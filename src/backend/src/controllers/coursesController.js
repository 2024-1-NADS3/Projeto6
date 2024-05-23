/** @module controllers/courses.js */

import { coursesServices } from "../services/coursesServices.js";
import Courses from "../models/Courses.js";

/**
 * Controller para criar um novo curso no sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém os dados do curso.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const createCourses = async (req, res) => {
  const partnerId = req.userId;

  const {
    title,
    type,
    category,
    description,
    address,
    zone,
    occupiedSlots,
    maxCapacity,
    initialDate,
    endDate,
  } = req.body;

  let img;

  switch (category) {
    case "Diversos":
      img = "/images/diversos.png";
      break;
    case "Música":
      img = "/images/musica.png";
      break;
    case "Informática":
      img = "/images/informatica.png";
      break;
    case "Esportes":
      img = "/images/esporte.png";
      break;
    default:
      img = "null";
      break;
  }

  const courses = new Courses(
    title,
    type,
    category,
    img,
    description,
    address,
    zone,
    occupiedSlots,
    maxCapacity,
    partnerId,
    initialDate,
    endDate,
  );

  const coursesCreated = await courses.create();

  if (!coursesCreated) {
    return res
      .status(500)
      .json({ message: "Não foi possível cadastrar o curso" });
  }

  console.log("Curso criado com sucesso:", coursesCreated);

  res.status(200).json({ message: "curso cadastrado com sucesso" });
};

/**
 * Controller para pegar todos os cursos registrados no sistema.
 * @param {Object} res - Objeto de resposta HTTP que retorna uma array de objetos do tipo curso.
 */
const getAllCourses = async (req, res) => {
  try {
    const courses = await coursesServices.getAllCourses();
    const reversedCourses = courses.reverse();
    res.status(200).json(reversedCourses);
  } catch (error) {
    console.log(error);
  }
};

/**
 * Controller para pegar as informações do curso parceiro.
 * @param {Object} req - Objeto de requisição HTTP que contém o id do curso.
 * @param {Object} res - Objeto de resposta HTTP que informa o resultado da requisição.
 */
const getCoursesById = async (req, res) => {
  const courseId = parseInt(req.params.Id);
  console.log("course id pelo params", courseId);
  console.log("tipo de dado course id pelo params", typeof courseId);

  try {
    const courses = await Courses.getCourseById(courseId);
    return res.status(200).send(courses);
  } catch (error) {
    console.log(error);
  }
};

const putCourseById = async (req, res) => {
  const {
    courseId,
    title,
    description,
    address,
    maxCapacity,
    initialDate,
    endDate,
    zone,
  } = req.body;

  try {
    const updatedCourse = {
      courseId,
      title,
      description,
      address,
      maxCapacity,
      initialDate,
      endDate,
      zone,
    };

    const updatedCourses =
      await coursesServices.UpdateCourseById(updatedCourse);

    console.log("Curso atualizado com sucesso:", updatedCourses);

    res.status(200).json(updatedCourses);
  } catch (error) {
    console.error("Erro ao atualizar o curso:", error);
    res
      .status(500)
      .json({ message: "Erro interno do servidor ao atualizar o curso" });
  }
};

export const coursesController = {
  getAllCourses,
  createCourses,
  getCoursesById,
  putCourseById,
};
