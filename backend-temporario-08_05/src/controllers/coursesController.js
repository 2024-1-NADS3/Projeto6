/** @module controllers/courses.js */

import { coursesServices } from "../services/coursesServices.js";

/**
 * Controller para pegar todos os cursos registrados no sistema.
 * @param {Object} res - Objeto de resposta HTTP que retorna uma array de objetos do tipo curso.
 */
const getAllCourses = async (req, res) => {
  try {
    const courses = await coursesServices.getAllCourses();  
    res.status(200).json(courses);
  } catch(error) {
    console.log(error)
  }
};

export const coursesController = {
  getAllCourses 
}

