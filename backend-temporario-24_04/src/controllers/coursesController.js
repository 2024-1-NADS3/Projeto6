import { coursesServices } from "../services/coursesServices.js";

const getAllCourses = async (req, res) => {
  try {
    const courses = await coursesServices.getAllCourses();  
    res.status(200).json(courses);
  } catch(error) {
    console.log(error)
  }
};

// export const deleteCourse = async (req, res) => {
//   const courseName = req.params.curso;
//   try {
//     const deletedCourse = await deleteCourseByName(courseName);
//     if (deletedCourse === 0) {
//       return res
//         .status(404)
//         .send(`Curso com o nome (${courseName}) não existe!`);
//     }
//     res.status(200).send(`Curso com o nome (${courseName}) deletado!`);
//   } catch (err) {
//     res.status(500).send("Problema generalizado no servidor interno.");
//   }
// };

// export const createCourse = async (req, res) => {
//   const formData = req.body;
//   const imagePath = req.file.path;

//   const courseExist = await getCourseByName(formData.course_name);

//   if (courseExist) {
//     return res
//       .status(404)
//       .send(`O curso com o nome (${formData.course_name}) já existe!`);
//   }

//   try {
//     await createCourseService(formData, imagePath);
//     res.status(200).send(`Curso criado com sucesso!`);
//   } catch (error) {
//     res.status(500).send(error.message);
//   }
// };

// export const updateCourse = async (req, res) => {
//   const courseName = req.params.curso;
//   const updatedCourse = req.body;

//   try {
//     const courseExist = await getCourseByName(courseName);

//     if (!courseExist) {
//       return res.status(404).send(`O curso (${courseName}) não existe!`);
//     }

//     const update = await updateCourseByName(courseName, updatedCourse);

//     res.status(200).send("Atualização feita com sucesso!");
//   } catch (error) {
//     res.status(500).send("Erro interno do servidor.");
//   }
// };

export const coursesController = {
  getAllCourses 
}