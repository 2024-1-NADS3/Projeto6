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

// AJUSTAR ESSA PARTE QUANDO FOR MEXER COM O FRONT, COLOCANDO O MIDDLEWARE
/**
 * Rota autenticada para registrar um usuário no curso escolhido.
 * @route POST /cadastrar
 */
router.post("/inscricao-curso", checkJwt, userController.userInscription);

/**
 * Rota autenticada para anular a inscrição do usuário em um curso específico.
 * @route delete /cadastrar
 */
router.delete("/anular-inscricao", checkJwt, userController.userInscription);


export default router;
