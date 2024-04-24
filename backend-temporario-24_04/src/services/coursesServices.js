import { db } from "../config/database/connection.js";

const getAllCourses = async () => {
  return new Promise((resolve, reject) => {
     db.all("SELECT * FROM cursos", [], (err, rows) => {
       if (err) {
         reject(err);
       } else {
         resolve(rows);
       }
     });
  });
 };

// export const getAllCourses = async () => {
//   try {
//     const [results, fields] = await connection.query(
//       'SELECT * FROM `cursos`'
//     );

//     console.log(results); // results contains rows returned by server
//     console.log(fields); // fields contains extra meta data about results, if available
//     return results;
//   } catch (err) {
//     console.log(err);
//   }
// }

export const coursesServices = {
  getAllCourses,
};
