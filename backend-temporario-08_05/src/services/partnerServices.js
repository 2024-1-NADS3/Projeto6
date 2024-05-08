import { db } from "../config/database/connection.js";

/** Obtém os dados do parceiro pelo email no banco de dados. */
const getPartnerByEmail = async (email) => {
  return new Promise((resolve, reject) => {
    db.get("SELECT * FROM partner WHERE email = ?", [email], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

/** Cria um novo parceiro no banco de dados. */
const createPartner = async (newPartner) => {
  const insertUserQuery = `
     INSERT INTO partner (instituitionName, cnpj, email, cellnumber, password)
     VALUES (?, ?, ?, ?, ?)
  `;

  return new Promise((resolve, reject) => {
    db.run(
      insertUserQuery,
      [
        newPartner.instituitionName,
        newPartner.cnpj,
        newPartner.email,
        newPartner.cellnumber,
        newPartner.password,
      ],
      function (err) {
        if (err) {
          reject(err);
        } else {
          resolve({ id: this.lastID, ...newPartner });
        }
      }
    );
  });
};

/** Deleta o usuário no sistema. */
const deleteUserById = async (userId) => {
  const sql = `DELETE FROM partner WHERE partnerId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [userId], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ message: "Usuário deletado com sucesso" });
      }
    });
  });
};

/** Pega o nome da instituição no banco de dados pelo id do curso */
const getInstituitionNameByCourseId = async (courseId) => {
  const sql =
    "SELECT p.instituitionName FROM courses AS c INNER JOIN partner AS p ON c.partnerId = p.partnerId WHERE c.courseId = ?;";

  return new Promise((resolve, reject) => {
    db.get(sql, [courseId], function (err, row) {
      if (err) {
        reject(err);
      } else {
        resolve(row ? row.instituitionName : null);
      }
    });
  });
};

/** Pega todos os cursos registrados pelo parceiro no bancdo de dados */
const getRegisteredCoursesByUserId = async (userId) => {
  const sql = "SELECT * FROM courses WHERE partnerId = ?";

  return new Promise((resolve, reject) => {
    db.all(sql, [userId], function (err, row) {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

/** Deleta o curso selecionado pelo parceiro  */
const deleteCourseByPartnerAndCourseId = (courseId) => {
  const sql = `DELETE FROM courses WHERE partnerId = ? AND courseId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [userId, courseId], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ message: "Usuário deletado com sucesso" });
      }
    });
  });
};

/** Pega todos os usuários inscritos em um curso específicado pelo parceiro  */
const getUsersRegisteredInTheCourseByCourseId = (courseId) => {
  const sql = `SELECT u.name, u.email, u.cellnumber FROM registrations as r 
  INNER JOIN user as u ON r.userId = u.userId
  WHERE courseId = ?;`;

  return new Promise((resolve, reject) => {
    db.all(sql, [courseId], function (err, row) {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

export const partnerServices = {
  getPartnerByEmail,
  createPartner,
  deleteUserById,
  getInstituitionNameByCourseId,
  getRegisteredCoursesByUserId,
  deleteCourseByPartnerAndCourseId,
  getUsersRegisteredInTheCourseByCourseId,
};
