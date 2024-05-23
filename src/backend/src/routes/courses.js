/** @module routes/courses.js */

import express from "express";
const router = express.Router();
import { checkJwt } from "../middleware/authMiddleware.js";
import { coursesController } from "../controllers/coursesController.js";

/**
 * Rota para cadastrar um novo curso.
 * @route POST /cadastrar
 */
router.post("/cadastrar", checkJwt, coursesController.createCourses);

/**
 * Rota para obter todos os cursos.
 * @route GET /todos
 */
router.get("/todos", coursesController.getAllCourses);

/**
 * Rota que pega os dados de um curso
 * @route get /Id
 */
router.get("/:Id", coursesController.getCoursesById);

/**
 * Rota que atualizar um curso
 * @route get /Id
 */
router.put("/update", coursesController.putCourseById);

export default router;
