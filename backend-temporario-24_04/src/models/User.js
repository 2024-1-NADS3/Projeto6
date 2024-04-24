import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { userServices } from "../services/userServices.js";

class User {
  constructor(name, email, password, cellnumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.cellnumber = cellnumber;
  }

  async create() {
    const hashedPassword = await bcrypt.hash(this.password, 10);
    const newUser = {
      name: this.name,
      email: this.email,
      password: hashedPassword,
      cellnumber: this.cellnumber,
    };
    return await userServices.createUser(newUser);
  }

  static async findByEmail(email) {
    return await userServices.getUserByEmail(email);
  }


//   static async authenticate(email, password) {
//     const user = await User.findByEmail(email);
//     if (!user) {
//       throw new Error("Usuário não encontrado");
//     }
//     const match = await bcrypt.compare(password, user.password);
//     if (!match) {
//       throw new Error("Senha inválida");
//     }
//     const token = jwt.sign({ id: user.id, email: user.email }, process.env.JWT_SECRET, { expiresIn: '1h' });
//     return token;
//  }
}

export default User;


