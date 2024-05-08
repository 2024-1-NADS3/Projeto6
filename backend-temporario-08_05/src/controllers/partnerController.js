import Partner from "../models/Partner.js";

/**
 * Controller para criar um novo parceiro no sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém os dados do parceiro.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const createPartner = async (req, res) => {
  const { instituitionName, cnpj, email, cellnumber, password } = req.body;

  const partner = new Partner(
    instituitionName,
    cnpj,
    email,
    cellnumber,
    password
  );

  const partnerExist = await Partner.findByEmail(email);

  console.log(partnerExist);

  if (partnerExist) {
    return res.status(409).json({ message: "Email já cadastrado" });
  }

  const partnerCreated = await partner.create();

  if (!partnerCreated) {
    return res
      .status(500)
      .json({ message: "Não foi possível cadastrar o parceiro" });
  }

  console.log("parceiro criado com sucesso:", partnerCreated);

  res.status(200).json({ message: "parceiro cadastrado com sucesso" });
};
/**
 * Controller para  deletar o parceiro no sistema(BD).
 * @param {Object} req - Objeto de requisição HTTP que contém o id do usuário.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const deleteUser = async (req, res) => {
  const userId = req.userId;

  try {
    const result = await Partner.deletePartner(userId);
    console.log(result);
    return res.status(200).send({ message: "Usuário deletado com sucesso" });
  } catch (err) {
    console.log(err);
  }
};

/**
 * Controller para resgatar o nome da instituição parceira.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do curso.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const getInstituitionName = async (req, res) => {
  const courseId = req.query.id;
  console.log(courseId);
  console.log(typeof courseId);

  try {
    const instituitionName = await Partner.getInstituitionNameByCourseId(
      courseId
    );
    return res.status(200).send({ message: instituitionName });
  } catch (error) {
    console.log(error);
    
    return res.status(500).send({ message: "deu ruim" });
  }
};


/**
 * Controller para pegar todos os cursos registrados do parceiro no sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do parceiro.
 * @param {Object} res - Objeto de resposta HTTP que retorna uma array de objetos do tipo curso.
 */
const getRegisteredCourses = async (req, res) => {
  const userId = req.userId

  console.log("userIdDentro do registered", userId)

  try {
    const cursos = await Partner.getRegisteredCourses(userId);
    res.status(200).send(cursos)
  } catch (err) {
    console.error(err);
  }
};


/**
 * Controller para deletar um curso específicado pelo parceiro.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do parceiro e do curso.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const deleteCourse = async (req, res) => {
  const courseId = req.params.id;
  const userId = req.userId

  console.log("course id pelo params", courseId);
  console.log("tipo de dado course id pelo params", typeof courseId);

  try {
    const deletedCourse = await Partner.deleteCourseByPartnerAndCourseId(userId, courseId);
    console.log("Curso deletado: ", deletedCourse)
    res.status(200).send({ message: "Curso deletado com sucesso!"})
  } catch (err) {
    console.log(err);
  }

}


/**
 * Controller pegar todos os usuários inscritos em um curso específico.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do parceiro e do curso.
 * @param {Object} res - Retorna uma array de objetos, onde os objetos são informações do usuário (email, celular e email).
 */
const getUsersRegisteredInTheCourse = async (req, res) => {
  
  const courseId = req.params.id;
  
  console.log("course id pelo params", courseId);
  console.log("tipo de dado course id pelo params", typeof courseId);

  try {
    const users = await Partner.getUsersRegisteredInTheCourseByCourseId(courseId);
    console.log("Users: ", users)
    res.status(200).send(users)
  } catch (err) {
    console.log(err);
  }

}

export const partnerController = {
  createPartner,
  deleteUser,
  getInstituitionName,
  getRegisteredCourses,
  deleteCourse,
  getUsersRegisteredInTheCourse
};
