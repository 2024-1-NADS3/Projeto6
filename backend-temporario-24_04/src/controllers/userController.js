import User from "../models/User.js";

const createUser = async (req, res) => {
  try {
    const { name, email, password, cellnumber } = req.body;

    const user = new User(name, email, password, cellnumber);

    const userExist = await User.findByEmail(email);

    if (userExist) {
      return res.status(409).json({ message: "Email já cadastrado" });
    }

    // Cria o usuário com o password hashed
    const userCreated = await user.create();

    if (!userCreated) {
      return res
        .status(500)
        .json({ message: "Não foi possível cadastrar o usuário" });
    }

    console.log("Usuário criado com sucesso:", userCreated);

    res.status(200).json({ message: "Usuário cadastrado com sucesso" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Erro interno no servidor" });
  }
};

// const login = async (req, res) => {
//   try {
//     const { email, password } = req.body;

//     const user = await getUserByEmail(email);

//     if (!user) {
//       return res.status(400).send("Usuário não existe!");
//     }

//     const passwordMatch = await bcrypt.compare(password, user.password);

//     if (!passwordMatch) {
//       return res.status(400).send("Senha inválida");
//     }

//     const token = jwt.sign(
//       { id: user.id, name: user.name, user_type: user.user_type },
//       "mudarAChaveDepois",
//       {
//         expiresIn: "1h",
//       }
//     );

//     res.status(200).send({ auth: true, token: token });
//   } catch (err) {
//     res.status(500).send(`Problema generalizado no servidor interno`);
//   }
// };

export const userController = {
  createUser,
};
