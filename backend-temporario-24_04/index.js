import 'dotenv/config';
import express from "express";
import cors from "cors";
import userRoute from "./src/routes/user.js";
import partnerRoute from "./src/routes/partner.js"
import coursesRoute from "./src/routes/courses.js"

const port = process.env.PORT || 3000;
const app = express();
app.use(cors());
app.use(express.json());

import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

app.use('/images', express.static(path.join(__dirname, 'src/images')));

app.use("/parceiro", partnerRoute)
app.use("/usuario", userRoute);
app.use("/cursos", coursesRoute);

app.listen(port, console.log(`Servidor rodando na porta ${port}`));


  