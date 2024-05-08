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

export const coursesServices = {
  getAllCourses,
  getCourseInitialDate,
  getOccupiedSlotsById,
  getMaxCapacityById,
  reserveCourseSpotById,
};
