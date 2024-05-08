import User from "../models/User.js";
import Partner from "../models/Partner.js";

/**
 * Controller com a lógica de login do sistema.
 * @param {Object} req - Objeto de requisição HTTP que contém o email e a senha do usuário.
 * @param {Object} res - Objeto de resposta HTTP que retorna o token (JWT) de autenticação.
 */
const login = async (req, res) => {
  const { email, password } = req.body;

  console.log(email, password);

  const user = await User.findByEmail(email);

  console.log("User: ", user);

  if (user) {
    try {
      const token = await User.authenticate(user, password);
      console.log("Token: ", token);
      return res.status(200).send({ token: token });
    } catch (err) {
      return res.status(401).send({ error: err.message });
    }
  }

  const partner = await Partner.findByEmail(email);

  if (partner) {
    try {
      const token = await Partner.authenticate(partner, password);
      console.log("Token partner:", token);
      return res.status(200).send({ token: token });
    } catch (err) {
      return res.status(401).send({ error: err.message });
    }
  }

  if (!user && !partner) {
    return res
      .status(401) // ver status correto dps
      .send({ error: "Nenhum usuário com esse email foi encontrado." });
  }

  return res.status(500).send({ error: "Erro interno no servidor" });
};

export const loginController = {
  login,
};
