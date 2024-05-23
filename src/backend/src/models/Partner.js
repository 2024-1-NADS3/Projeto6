import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { partnerServices } from "../services/partnerServices.js";

/**
 *  @class 
 * Classe que representa o parceiro no sistema 
*/
class Partner {
  constructor(instituitionName, cnpj, email, cellnumber, password) {
    this.instituitionName = instituitionName;
    this.cnpj = cnpj;
    this.email = email;
    this.cellnumber = cellnumber;
    this.password = password;
  }

  /** Cria um novo parceiro no sistema. */
  async create() {
    const hashedPassword = await bcrypt.hash(this.password.trim(), 10);
    const newPartener = {
      instituitionName: this.instituitionName,
      cnpj: this.cnpj,
      email: this.email,
      password: hashedPassword,
      cellnumber: this.cellnumber,
    };
    return await partnerServices.createPartner(newPartener);
  }

  /** Deleta um parceiro do sistema */
  static async deletePartner(userId) {
    return await partnerServices.deleteUserById(userId);
  }

  static async unsubscribreTheUserByUserIdAndcourseId(userId, courseId) {
    return await partnerServices.unsubscribreTheUserByUserIdAndcourseId(
      userId,
      courseId
    );
  }

  /** Método que encontra um parceiro pelo email. */
  static async findByEmail(email) {
    return await partnerServices.getPartnerByEmail(email);
  }

  /** Método que pega todos os cursos registrados do parceiro */
  static async getRegisteredCourses(userId) {
    return await partnerServices.getRegisteredCoursesByUserId(userId);
  }

  /** Método que pega o nome da instituição */
  static async getInstituitionNameByCourseId(courseId) {
    const courseIdAsInteger = Number(courseId);
    return await partnerServices.getInstituitionNameByCourseId(
      courseIdAsInteger
    );
  }

  /** Método que pega todos os usuários registrado em um curso do parceiro */
  static async getUsersRegisteredInTheCourseByCourseId(courseId) {
    return await partnerServices.getUsersRegisteredInTheCourseByCourseId(
      courseId
    );
  }

  /** Método que deleta um curso selecionado pelo usuário */
  static async deleteCourseByPartnerAndCourseId(userId, courseId) {
    return await partnerServices.deleteCourseByPartnerAndCourseId(
      userId,
      courseId
    );
  }

  /** Método pegar o nome do usuário no sistema */
  static async getPartnerNameById(userId) {
    return await partnerServices.getPartnerNameById(userId);
  }

  /** Autentica o parceiro, retornando um token JWT */
  static async authenticate(partner, password) {
    console.log("dentro do authenticate: ", password, partner);

    const match = await bcrypt.compare(
      password.trim(),
      partner.password.trim()
    );

    if (!match) {
      throw new Error("Senha inválida");
    }
    const token = jwt.sign(
      { userId: partner.partnerId, email: partner.email, userType: "partner" },
      process.env.JWT_SECRET,
      { expiresIn: "1h" }
    );
    return token;
  }
}

export default Partner;
