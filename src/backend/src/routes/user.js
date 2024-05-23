/** @module routes/user.js */

import express from "express";
const router = express.Router();
import { checkJwt } from "../middleware/authMiddleware.js";
import { userController } from "../controllers/userController.js";

/**
 * Rota para cadastrar um novo usuário.
 * @route POST /cadastrar
*/
router.post("/cadastrar", userController.createUser);

/**
 * Rota autenticada para deletar um usuário do sistema.
 * @route POST /cadastrar
*/
router.delete("/deletar-usuario", checkJwt, userController.deleteUser);

/**
 * Rota autenticada para registrar um usuário no curso escolhido.
 * @route POST /cadastrar
*/
router.post("/inscricao-curso", checkJwt, userController.userInscription);

/**
 * Rota para pegar todos os cursos inscritos do usuário.
 * @route GET /cadastrar
*/
router.get("/cursos-inscritos", checkJwt, userController.userCourses)

/**
 * Rota para pegar a quantidade de advertencias que o usuário tem.
 * @route GET /cadastrar
*/
router.get("/pegar-advertencias", checkJwt, userController.getUserWarnings)

/**
 * Rota para anular a inscrição em um curso escolhido pelo usuário.
 * @route GET /cadastrar
*/
router.delete("/anular-inscricao/curso/:courseId", checkJwt, userController.userUnsubscription);

/**
 * Rota para resgatar o nome do usuário.
 * @route GET /pegar-nome-do-usuario
*/
router.get("/pegar-nome-do-usuario", checkJwt, userController.getUserName)

export default router;
