import 'dotenv/config';
import express from "express";
import cors from "cors";
import loginRoute from "./src/routes/login.js"
import userRoute from "./src/routes/user.js";
import partnerRoute from "./src/routes/partner.js"
import coursesRoute from "./src/routes/courses.js"
import { checkJwt } from './src/middleware/authMiddleware.js';

/** Configuração da porta e do servidor Express. */
const port = process.env.PORT || 3000;
const app = express();
app.use(cors());
app.use(express.json());

/** Configuração do diretório de imagens estáticas. */
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

app.use('/images', express.static(path.join(__dirname, 'src/images')));

/** Configuração das rotas da aplicação. */
app.use("/parceiro", partnerRoute)
app.use("/usuario", userRoute);
app.use("/cursos", coursesRoute);
app.use("/login", loginRoute);

/** Rota para pegar o tipo de usuário que está logado no sistema  */
app.get("/user-type", checkJwt, (req, res) => {
  
  try {
    const userType = req.userType;
    res.status(200).json({ userType });
  } catch (err) {
    res.status(404).json({ message: err })
  }
  
})

app.listen(port, console.log(`Servidor rodando na porta ${port}`));


  