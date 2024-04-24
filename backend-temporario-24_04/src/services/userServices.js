import { db } from "../config/database/connection.js";

const getUserByEmail = async (email) => {
  return new Promise((resolve, reject) => {
    db.get("SELECT * FROM user WHERE email = ?", [email], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row ? true : false);
      }
    });
  });
};

const createUser = async (newUser) => {
  const insertUserQuery = `
     INSERT INTO user (name, email, password, cellnumber)
     VALUES (?, ?, ?, ?)
  `;
 
  return new Promise((resolve, reject) => {
     db.run(insertUserQuery, [newUser.name, newUser.email, newUser.password, newUser.cellnumber], function (err) {
       if (err) {
         reject(err);
       } else {
         resolve({ id: this.lastID, ...newUser }); 
       }
     });
  });
 };

export const userServices = {
  getUserByEmail,
  createUser,
};
