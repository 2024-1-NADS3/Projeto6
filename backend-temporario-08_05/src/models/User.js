import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { userServices } from "../services/userServices.js";

/** Classe User representa um usuário normal da aplicação. */
class User {
  constructor(name, email, password, cellnumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.cellnumber = cellnumber;
  }

  /** Cria um novo usuário no sistema. */
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

  /** Deleta um usuário do sistema */
  static async deleteUser(userId) {
    return await userServices.deleteUserById(userId);
  }

  /** Encontra um usuário pelo email. */
  static async findByEmail(email) {
    return await userServices.getUserByEmail(email);
  }

  /** Inscreve o usuário no curso escolhido */
  static async registerUserInTheCourse(userId, courseId) {
    return await userServices.registerUserInTheCourse(userId, courseId);
  }

  /** Checa se o usuário já está inscrito em um curso específico */
  static async courseRegistrationCheck(userId, courseId) {
    return await userServices.courseRegistrationCheck(userId, courseId);
  }
  
  /** Autentica o usuário, retornando um token JWT  */
  static async authenticate(user, password) {
    console.log("dentro do authenticate", user, password);

    const match = await bcrypt.compare(password, user.password);
    console.log(match);

    if (!match) {
      throw new Error("Senha inválida");
    }

    const token = jwt.sign(
      { userId: user.userId, email: user.email, userType: "user" },
      process.env.JWT_SECRET,
      { expiresIn: "1h" }
    );
    return token;
  }
}

export default User;
