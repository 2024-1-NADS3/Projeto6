/** @module index.js */

import "dotenv/config";
import express from "express";
import cors from "cors";
import nodemailer from "nodemailer"
import bcrypt from "bcryptjs";
import loginRoute from "./src/routes/login.js";
import userRoute from "./src/routes/user.js";
import partnerRoute from "./src/routes/partner.js";
import coursesRoute from "./src/routes/courses.js";
import { checkJwt } from "./src/middleware/authMiddleware.js";
import { allUserTypesServices } from "./src/services/allUserTypesServices.js";
import User from "./src/models/User.js";
import Partner from "./src/models/Partner.js";

/** Configuração da porta e do servidor Express. */
const port = process.env.PORT || 3000;
const app = express();
app.use(cors());
app.use(express.json());

/** Configuração do diretório de imagens estáticas. */
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

app.use("/images", express.static(path.join(__dirname, "src/images")));

/** Configuração das rotas da aplicação. */
app.use("/parceiro", partnerRoute);
app.use("/usuario", userRoute);
app.use("/cursos", coursesRoute);
app.use("/login", loginRoute);

/**
 * Rota para verificar o tipo de usuário no sistema.
 * @route GET /pegar-codigo
*/
app.get("/user-type", checkJwt, (req, res) => {
  try {
    const userType = req.userType;
    res.status(200).json({ userType });
  } catch (err) {
    res.status(500).json({ message: err }); // Mudar dps o status code
  }
});


/**
 * Rota que gera um código para poder recuperar a senha.
 * @route POST /pegar-codigo
*/
app.post("/pegar-codigo", async (req, res) => {
  const { email } = req.body;

  console.log(email);

  const isUser = await User.findByEmail(email);

  const isParceiro = await Partner.findByEmail(email);

  if (!isParceiro && !isUser) {
    return res
      .status(400)
      .send({ message: "Email não consta no banco de dados" });
  }

  const code = GerarCodigoAleatorio();

  // Configurando transporte
  let transporter = nodemailer.createTransport({
    service: "outlook", //Pode ser qualquer provedor de email
    auth: {
      user: process.env.EMAIL_PROJECT,
      pass: process.env.EMAIL_PASSWORD,
    },

  });

  //Detalhes do email a ser enviado
  let mailOptions = {
    from: "educalizaprojeto@outlook.com", //email remetente
    to: email, //email destinatario
    subject: "Redefinição de senha", //Assunto
    text:
      "Olá, este é o código de confirmação para redefinir sua senha: " + code, //texto
  };

  try {
    const info = await transporter.sendMail(mailOptions);
    console.log("E-mail enviado: " + info.response);
    res.status(200).send({
      code,
      email,
    });
  } catch (error) {
    console.log("Erro ao enviar o e-mail:", error);
    res.status(500).send({
      message: "Falha ao enviar o e-mail, por favor tente novamente mais tarde.",
      error: error.message,
    });
  }
});


/**
 * Rota para atualizar a senha antiga do usuário por uma nova.
 * @route PUT /nome-instituicao
*/
app.put("/atualizar-senha", async (req, res) => {
  const { email, newPassword } = req.body;

  console.log("Estou no atualizar senha")
  console.log(email, newPassword);

  const hashedPassword = await bcrypt.hash(newPassword, 10);

  const isUser = await User.findByEmail(email);

  if (isUser) {
    try {
      const result = await allUserTypesServices.changeUserPasswordByEmail(
        "user",
        email,
        hashedPassword
      );

      console.log(result);

      return res.status(200).send({ message: "Senha modificado com sucesso" });
    } catch (err) {
      console.error(err);
      res.status(500).send({ message: err });
    }
  }

  const isParceiro = await Partner.findByEmail(email);

  if (isParceiro) {
    try {
      const result = await allUserTypesServices.changeUserPasswordByEmail(
        "partner",
        email,
        hashedPassword
      );

      console.log(result);

      return res.status(200).send({ message: "Senha modificado com sucesso" });
    } catch (err) {
      console.error(err);
      res.status(500).send({ message: err });
    }
  }
});

/** Função para gerar um código aleatório */
function GerarCodigoAleatorio() {
  // Gera um número aleatório entre 0 (inclusivo) e 1 (exclusivo)
  let numeroAleatorio = Math.random();

  // Multiplica pelo intervalo desejado (999999 - 100000 + 1) e adiciona o valor mínimo (100000)
  numeroAleatorio *= 900000;

  // Arredonda para baixo para garantir que o resultado seja um número inteiro
  numeroAleatorio = Math.floor(numeroAleatorio);

  // Adiciona o valor mínimo ao resultado para garantir que o código comece com 100000
  numeroAleatorio += 100000;

  // Retorna o código aleatório de 6 dígitos
  return numeroAleatorio;
}

app.listen(port, console.log(`Servidor rodando na porta ${port}`));
