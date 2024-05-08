/** @module routes/courses.js */

import express from "express";
import { coursesController } from "../controllers/coursesController.js";

const router = express.Router();

/**
 * Rota para obter todos os cursos.
 * @route GET /todos
*/ 
router.get("/todos", coursesController.getAllCourses);

export default router;
