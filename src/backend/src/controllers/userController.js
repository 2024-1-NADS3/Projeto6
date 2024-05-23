import User from "../models/User.js";
import { coursesServices } from "../services/coursesServices.js";

/**
 * Controller para criar um novo usuário no sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém os dados do usuário.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const createUser = async (req, res) => {
  try {
    const { name, email, password, cellnumber } = req.body;

    const user = new User(name, email, password, cellnumber);

    const userExist = await User.findByEmail(email);

    if (userExist) {
      return res.status(409).json({ message: "Email já cadastrado" });
    }

    // Cria o usuário com o password hashed
    const userCreated = await user.create();

    if (!userCreated) {
      return res
        .status(500)
        .json({ message: "Não foi possível cadastrar o usuário" });
    }

    console.log("Usuário criado com sucesso:", userCreated);

    res.status(200).json({ message: "Usuário cadastrado com sucesso" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Erro interno no servidor" });
  }
};

/**
 * Controller para deletar o usuário no sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const deleteUser = async (req, res) => {
  const userId = req.userId;

  console.log(userId);
  console.log(typeof userId);

  try {
    const result = await User.deleteUser(userId);
    console.log(result);
    res.status(200).send({ message: "Usuário deletado com sucesso" });
  } catch (err) {
    console.log(err);
  }
};

/** Função que checa se a data atual é maior que a do curso */
const checkCourseDeadline = async (courseId) => {
  const date = new Date();
  const year = date.getFullYear();
  const month = date.getMonth(); // Lembre-se que getMonth() retorna um valor de 0 a 11
  const day = date.getDate();

  try {
    const courseInitialDate = await coursesServices.getCourseInitialDate(courseId);
    const [initialDay, initialMonth, initialYear] = courseInitialDate[0].initialDate.split('/').map(Number);
    
    // Cria as datas usando o construtor new Date(year, monthIndex, day)
    const courseDate = new Date(initialYear, initialMonth - 1, initialDay);
    const currentDate = new Date(year, month, day);

    console.log(courseInitialDate[0].initialDate);
    console.log(courseDate);
    console.log(currentDate);

    if (currentDate < courseDate) {
      // A data atual é anterior à data estipulada, o usuário pode se inscrever
      return false;
    } else {
      // A data atual é posterior à data estipulada, o usuário não pode se inscrever
      return true;
    }
  } catch (err) {
    console.error(err);
    throw err; // Lança o erro para que ele possa ser tratado na função userInscription
  }
};

/**
 * Controller para fazer a inscrição do usuário no curso escolhido pelo mesmo.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário, o tipo de usuário, e o id do curso escolhido.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const userInscription = async (req, res) => {
  const { courseId } = req.body;
  const userId = req.userId;
  const userType = req.userType;

  console.log("cheguei no controller");

  if (userType === "partner") {
    return res.status(403).send({
      message: "Parceiros não podem se inscrever em cursos.",
    });
  }

  try {
    const userWarnings = await User.getUserWarningsById(userId);

    if (userWarnings.warnings >= 5) {
      return res.status(403).send({
        message: "Inscrição negada, o número limite de advertências já foi alcançado."
      })
    }

    const currentDateExceeded = await checkCourseDeadline(courseId);

    if (currentDateExceeded) {
      return res.status(400).send({
        message:
          "Não foi possível fazer a inscrição. Data de inicio do curso ou aula já chegou",
      });
    }

    const isUserAlreadyRegistered = await User.courseRegistrationCheck(
      userId,
      courseId
    );

    if (isUserAlreadyRegistered) {
      return res
        .status(400)
        .send({ message: "Você já está inscrito neste curso." });
    }

    // get ocuppiedSlots e maxCapacity para ver se já está cheio

    const occupiedSlots = await coursesServices.getOccupiedSlotsById(courseId);
    const maxCapacity = await coursesServices.getMaxCapacityById(courseId);

    console.log(
      "occupiedSlots",
      occupiedSlots[0].occupiedSlots,
      typeof occupiedSlots
    );
    console.log("maxCapacity", maxCapacity[0].maxCapacity, typeof maxCapacity);

    if (occupiedSlots[0].occupiedSlots >= maxCapacity[0].maxCapacity) {
      return res
        .status(400)
        .send({ message: "Todas as vagas do curso já foram preenchidas" });
    }

    const registeredUser = await User.registerUserInTheCourse(userId, courseId);

    if (registeredUser) {
      const resultReserve = await coursesServices.reserveCourseSpotById(
        courseId
      );
      console.log("resultReserve: ", resultReserve);
    }

    // aqui teria que ter a lógica para acrescentar 1 na coluna ocuppiedSlots

    // verificar se deu certo isso aqui depois
    res.status(201).send({ message: registeredUser });
  } catch (err) {
    console.log(err);
  }
};

/**
 * Controller para pegar os cursos que o usuário está inscrito.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário.
 * @param {Object} res - Objeto de resposta HTTP que retorna uma array de objetos, onde cada objeto é um curso.
 */
const userCourses = async (req, res) => {
  const userId = req.userId;

  console.log("userIdDentro do registered", userId);

  try {
    const cursos = await User.getUserCourses(userId);
    const reversedCourses = cursos.reverse();
    res.status(200).send(reversedCourses);
  } catch (err) {
    console.error(err);
  }
};

/**
 * Controller para desinscrever o usuário em um curso que foi escolhido pelo mesmo.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário e o id do curso.
 * @param {Object} res - Não tem corpo de resposta, apenas um status code como resposta.
 */
const userUnsubscription = async (req, res) => {
  const userId = req.userId;

  const { courseId } = req.params;

  console.log("userIdDentro: ", userId);
  console.log("courseId", courseId);

  try {
    await User.userUnsubscribeByUserIdAndCourseId(
      userId,
      courseId
    );
  
    res.status(200).send({ message: "Desinscrição feita com sucesso" })
  } catch (err) {
    console.error(err);
  }
};

/**
 * Controller para pegar a quantidade de advertências do usuário.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário.
 * @param {Object} res - Objeto de resposta HTTP que retorna uma array de objetos, onde cada objeto é um curso.
 */
const getUserWarnings = async (req, res) => {
  const userId = req.userId;

  try {
    const warnings = await User.getUserWarningsById(userId);
    res.status(200).send(warnings);
  } catch (err) {
    console.error(err);
  }
};

/**
 * Controller para pegar a quantidade de advertências do usuário.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário.
 * @param {Object} res - Objeto de resposta HTTP que retorna o nome do usuário.
 */
const getUserName = async (req, res) => {
  const userId = req.userId;

  try {
    const userName = await User.getUserNameById(userId);
    res.status(200).send(userName);
  } catch (err) {
    console.error(err);
  }
}

export const userController = {
  createUser,
  deleteUser,
  userInscription,
  userUnsubscription,
  userCourses,
  getUserWarnings,
  getUserName
};
