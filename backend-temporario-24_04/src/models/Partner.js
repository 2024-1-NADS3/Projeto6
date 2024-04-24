import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { partnerServices } from "../services/partnerServices.js";

class Partner {
  constructor(instituitionName, cnpj, email, password, cellnumber) {
    this.instituitionName = instituitionName;
    this.cnpj = cnpj;
    this.email = email;
    this.password = password;
    this.cellnumber = cellnumber;
  }

  async create() {
    const hashedPassword = await bcrypt.hash(this.password, 10);
    const newPartener = {
      instituitionName: this.instituitionName,
      cnpj: this.cnpj,
      email: this.email,
      password: hashedPassword,
      cellnumber: this.cellnumber,
    };
    return await partnerServices.createPartener(newPartener);
  }

  static async findByEmail(email) {
    return await partnerServices.getPartenerByEmail(email);
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

export default Partner