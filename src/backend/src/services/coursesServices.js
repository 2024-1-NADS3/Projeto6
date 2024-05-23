import { db } from "../config/database/connection.js";

/** Obtém todos os cursos do banco de dados. */
const getAllCourses = async () => {
  return new Promise((resolve, reject) => {
    db.all("SELECT * FROM courses", [], (err, rows) => {
      if (err) {
        reject(err);
      } else {
        resolve(rows);
      }
    });
  });
};

/** Obtém a data de inicio do curso no banco de dados. */
const getCourseInitialDate = async (courseId) => {
  const sql = `SELECT initialDate FROM courses WHERE courseId = ?`;

  return new Promise((resolve, reject) => {
    db.all(sql, [courseId], (err, rows) => {
      if (err) {
        reject(err);
      } else {
        resolve(rows);
      }
    });
  });
};

/** Obtém a quantidade de vagas ocupadas em um curso no banco de dados. */
const getOccupiedSlotsById = async (courseId) => {
  const sql = `SELECT occupiedSlots FROM courses WHERE courseId = ?`;

  return new Promise((resolve, reject) => {
    db.all(sql, [courseId], (err, rows) => {
      if (err) {
        reject(err);
      } else {
        resolve(rows);
      }
    });
  });
};

/** Obtém a capacidade máxima de vagas do curso  no banco de dados */
const getMaxCapacityById = async (courseId) => {
  const sql = `SELECT maxCapacity FROM courses WHERE courseId = ?`;

  return new Promise((resolve, reject) => {
    db.all(sql, [courseId], (err, rows) => {
      if (err) {
        reject(err);
      } else {
        resolve(rows);
      }
    });
  });
};

/** Obtém a capacidade máxima de vagas do curso  no banco de dados */
const reserveCourseSpotById = async (courseId) => {
  const sql = `UPDATE courses SET occupiedSlots = occupiedSlots + 1 WHERE courseId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [courseId], function (err) {
      if (err) {
        reject(err);
      } else {
        // Utilize a função changes() diretamente após a execução da consulta
        resolve({ changes: this.changes });
      }
    });
  });
};

/** Libera uma vaga no curso, atualizando occupiedslots po occupiedslotsr - 1 */
const releaseCourseSpotById = async (courseId) => {
  const sql = `UPDATE courses SET occupiedSlots = occupiedSlots - 1 WHERE courseId = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [courseId], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ changes: this.changes });
      }
    });
  });
};

const createCourses = async (newCourses) => {
  const insertUserQuery = `
     INSERT INTO courses (title, type, category, img, description, address, zone, occupiedSlots, maxCapacity, partnerid, initialDate, endDate)
     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `;

  return new Promise((resolve, reject) => {
    db.run(
      insertUserQuery,
      [
        newCourses.title,
        newCourses.type,
        newCourses.category,
        newCourses.img,
        newCourses.description,
        newCourses.address,
        newCourses.zone,
        newCourses.occupiedSlots,
        newCourses.maxCapacity,
        newCourses.partnerId,
        newCourses.initialDate,
        newCourses.endDate,
      ],
      function (err) {
        if (err) {
          reject(err);
        } else {
          resolve({ id: this.lastID, ...newCourses });
        }
      },
    );
  });
};

/** Obtém os dados do curso pelo Id no banco de dados. */
const getCoursesById = async (courseId) => {
  const sql = "SELECT * FROM courses WHERE courseId = ?";

  return new Promise((resolve, reject) => {
    db.get(sql, [courseId], function (err, row) {
      if (err) {
        console.error("Erro na consulta:", err);
        reject(err);
      } else {
        console.log("Resultado da consulta:", row);
        resolve(row);
      }
    });
  });
};

/** Atualiza um curso  */
const UpdateCourseById = async (putCourses) => {
  const sql = `UPDATE courses 
  SET title = ?, 
      description = ?, 
      address = ?, 
      maxCapacity = ?, 
      initialDate = ?, 
      endDate = ?,
      zone = ?
  WHERE courseId = ?;`;

  const values = [
    putCourses.title,
    putCourses.description,
    putCourses.address,
    putCourses.maxCapacity,
    putCourses.initialDate,
    putCourses.endDate,
    putCourses.zone,
    putCourses.courseId,
  ];

  return new Promise((resolve, reject) => {
    db.run(sql, values, function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ changes: this.changes });
      }
    });
  });
};

export const coursesServices = {
  getAllCourses,
  getCourseInitialDate,
  getOccupiedSlotsById,
  getMaxCapacityById,
  reserveCourseSpotById,
  releaseCourseSpotById,
  createCourses,
  getCoursesById,
  UpdateCourseById,
};
