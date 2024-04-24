import express from "express";
const router = express.Router();
import { partnerController } from "../controllers/partnerController.js";

router.post("/cadastrar", partnerController.createPartner);

export default router