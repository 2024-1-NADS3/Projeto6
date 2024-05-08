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
  console.log(typeof userId)

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
  const month = String(date.getMonth() + 1).padStart(2, "0"); // Adiciona um zero à esquerda se o mês for um dígito
  const day = String(date.getDate()).padStart(2, "0"); // Adiciona um zero à esquerda se o dia for um dígito
  const currentDay = `${year}-${month}-${day}`;

  try {
    const courseInitialDate = await coursesServices.getCourseInitialDate(
      courseId
    );
    const courseDate = new Date(courseInitialDate[0].initialDate);
    const currentDate = new Date(currentDay);

    if (currentDate < courseDate) {
      // A data atual é anterior à data estipulada, o usuário pode se inscrever
      return false;
    } else {
      // A data atual é posterior à data estipulada, o usuário não pode se inscrever
      return true;
    }
  } catch (err) {
    console.error(err);
    throw err; // Lança o erro para que ele possa na função userInscription
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

  console.log("cheguei no controller")


  if (userType === "partner") {
    return res.status(403).send({
      message: "Parceiros não podem se inscrever em cursos.",
    });
  }

  try {
    const currentDateExceeded = await checkCourseDeadline(courseId);

    if (currentDateExceeded) {
      return res
        .status(400)
        .send({
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

    console.log("occupiedSlots", occupiedSlots[0].occupiedSlots, typeof occupiedSlots)
    console.log("maxCapacity", maxCapacity[0].maxCapacity, typeof maxCapacity)

    if(occupiedSlots[0].occupiedSlots >= maxCapacity[0].maxCapacity) {
      return res.status(400).send({ message: "Todas as vagas do curso já foram preenchidas" });
    }

    const registeredUser = await User.registerUserInTheCourse(userId, courseId);

    if (registeredUser) {
      const resultReserve = await coursesServices.reserveCourseSpotById(courseId);
      console.log("resultReserve: ", resultReserve);
    } 

    // aqui teria que ter a lógica para acrescentar 1 na coluna ocuppiedSlots

    // verificar se deu certo isso aqui depois
    res.status(201).send({ message: registeredUser });
  } catch (err) {
    console.log(err);
  }
};

export const userController = {
  createUser,
  deleteUser,
  userInscription,
};
