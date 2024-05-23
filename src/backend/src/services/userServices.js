import { db } from "../config/database/connection.js";
import { coursesServices } from "./coursesServices.js";

/** Obtém um usuário pelo email do banco de dados. */
const getUserByEmail = async (email) => {
  return new Promise((resolve, reject) => {
    db.get("SELECT * FROM user WHERE email = ?", [email], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

/** Cria um novo usuário no banco de dados. */
const createUser = async (newUser) => {
  const insertUserQuery = `
     INSERT INTO user (name, email, password, cellnumber)
     VALUES (?, ?, ?, ?)
  `;

  return new Promise((resolve, reject) => {
    db.run(
      insertUserQuery,
      [newUser.name, newUser.email, newUser.password, newUser.cellnumber],
      function (err) {
        if (err) {
          reject(err);
        } else {
          resolve({ id: this.lastID, ...newUser });
        }
      }
    );
  });
};

/** Deleta o usuário do banco de dados pelo Id */
const deleteUserById = async (userId) => {
  console.log("Dentro do Service para deltar", userId);
  console.log(typeof userId);

  const sql = `DELETE FROM user WHERE userId = ?`;

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

/** Registra o usuário em um curso no banco de dados */
const registerUserInTheCourse = async (userId, courseId) => {
  const sql =
    "INSERT INTO registrations (courseId, creationDate, deletionDate, userId) VALUES (?, ?, ?, ?)";

  // Vereficiar depois se vai deixar creation e deletion date, e caso deixe, precisa ver a lógica de guardar data no bd
  return new Promise((resolve, reject) => {
    db.run(sql, [courseId, 0, 0, userId], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ message: "Inscrição feita com sucesso!" });
      }
    });
  });
};

/** Verfica se o usuário tem o registro de um usuário em alguma curso específico  */
const courseRegistrationCheck = (userId, courseId) => {
  const sql = "SELECT * FROM registrations WHERE userId = ? AND courseId = ?";
  let userAlreadyRegistered = false;

  return new Promise((resolve, reject) => {
    db.get(sql, [userId, courseId], function (err, row) {
      if (err) {
        reject(err);
      } else {
        if (row) {
          userAlreadyRegistered = true;
        }
        resolve(userAlreadyRegistered);
      }
    });
  });
};

/** Adiciona uma advertência para um usuário específico */
const addOneWarningByTheDeletionFromACourse = async (userId) => {
  const sql = `UPDATE user SET warnings = warnings + 1 WHERE userId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [userId], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ changes: this.changes });
      }
    });
  });
};

/** Resgata a quantidade de advertências de um usuário no bd */
const getUserWarningsById = async (userId) => {
  const sql = `SELECT warnings FROM user WHERE userId = ?;`;

  return new Promise((resolve, reject) => {
    db.get(sql, [userId], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

/** Resgata todos os cursos que o usuário está inscrito  */
const getUserCoursesByUserId = async (userId) => {
  const sql =
    "SELECT * FROM courses as c INNER JOIN registrations as r on r.courseId = c.courseId WHERE r.userId = ?;";

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

/** Resgata a inscrição de um usuário em um curso específico */
const userUnsubscribeByUserIdAndCourseId = async (userId, courseId) => {
  const sql = `DELETE FROM registrations WHERE userId = ? and courseId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [userId, courseId], function (err) {
      if (err) {
        reject(err);
      } else {
        coursesServices
          .releaseCourseSpotById(courseId)
          .then(() => {
            resolve();
          })
          .catch(() => {
            reject;
          });
      }
    });
  });
};

/** Resgata o nome do usuário  */
const getUserNameById = (userId) => {
  const sql = `SELECT name FROM user WHERE userId = ?;`;

  return new Promise((resolve, reject) => {
    db.get(sql, [userId], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });  
}

export const userServices = {
  getUserByEmail,
  createUser,
  deleteUserById,
  registerUserInTheCourse,
  courseRegistrationCheck,
  addOneWarningByTheDeletionFromACourse,
  getUserCoursesByUserId,
  userUnsubscribeByUserIdAndCourseId,
  getUserWarningsById,
  getUserNameById,
};
