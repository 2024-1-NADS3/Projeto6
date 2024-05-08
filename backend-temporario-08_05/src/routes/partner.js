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
router.delete("/deletar-curso/:id", checkJwt, partnerController.deleteCourse)

/**
 * Rota que pega todos os usuários inscritos em um curso específico
 * @route get /usuarios-cadastrados-no-curso
*/
router.get("/usuarios-cadastrados-no-curso/:id", partnerController.getUsersRegisteredInTheCourse)

// /**
//  * Rota para criar um curso na plataforma
//  * @route post /cadastrar-cursos
// */
// router.post("/cadastrar-curso", partnerController.registerNewCourse)

// /**
//  * Rota para o parceiro atualizar os dados de algum curso específico 
//  * @route PUT /atualizar-curso
// */
// router.put("/atualizar-curso", partnerController.updateCourse)


export default router