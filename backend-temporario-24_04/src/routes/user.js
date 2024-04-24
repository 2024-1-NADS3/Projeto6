import express from "express";
const router = express.Router();
import { userController } from "../controllers/userController.js";

// // router.post("/cadastrar/parceiro", userController.createPartner);
router.post("/cadastrar", userController.createUser);
// // router.post("/login", userController.login);

export default router;
