/** @module routes/login.js */

import express from "express";
const router = express.Router();
import { loginController } from "../controllers/loginController.js";

/**
 * Rota para fazer o login no sistema.
 * POST /cadastrar
*/
router.post("/", loginController.login);

export default router