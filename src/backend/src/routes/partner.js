/** @module routes/partner.js */

import express from "express";
const router = express.Router();
import { checkJwt } from '../middleware/authMiddleware.js';
import { partnerController } from "../controllers/partnerController.js";

/**
 * Rota para cadastrar um novo parceiro.
 * @route POST /cadastrar
*/
router.post("/cadastrar", partnerController.createPartner);

/**
 * Rota para resgatar o nome da instituição referente ao curso selecionado pelo usuário.
 * @route GET /nome-instituicao
*/
router.get("/nome-instituicao", partnerController.getInstituitionName);

/**
 * Rota para excluir um parceiro do banco de dados.
 * @route delete /deletar-parceiro
*/
router.delete("/deletar-parceiro", checkJwt, partnerController.deleteUser)

/**
 * Rota retornar todos os cursos cadastrados pelo parceiro na plataforma.
 * @route get /cursos-cadastrados
*/
router.get("/cursos-cadastrados", checkJwt, partnerController.getRegisteredCourses)

/**
 * Rota que deleta um curso selecionado pelo parceiro.
 * @route delete /cursos-cadastrados
*/
router.delete("/deletar-curso/:id", checkJwt, partnerController.deleteCourse);

/**
 * Rota que pega todos os usuários inscritos em um curso específico
 * @route get /usuarios-cadastrados-no-curso
*/
router.get("/usuarios-cadastrados-no-curso/:id", checkJwt, partnerController.getUsersRegisteredInTheCourse);

/**
 * Rota para tirar a inscrição de um usuário em um curso específico
 * @route delete /usuarios-cadastrados-no-curso
*/
router.delete("/tirar-inscricao-do-usuario/curso/:courseId/usuario/:userId", checkJwt, partnerController.unsubcribeUserFromCourse);

/**
 * Rota para pegar o nome da instituição
 * @route get /usuarios-cadastrados-no-curso
*/
router.get("/pegar-nome-instituicao", checkJwt, partnerController.getPartnerName)


export default router