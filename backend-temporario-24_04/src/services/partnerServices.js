import { db } from "../config/database/connection.js";

const getPartenerByEmail = async (email) => {
  return new Promise((resolve, reject) => {
    db.get("SELECT * FROM partner WHERE email = ?", [email], (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row ? true : false);
      }
    });
  });
};

const createPartener = async (newPartner) => {
  const insertUserQuery = `
     INSERT INTO partner (instituitionName, cnpj, email, password, cellnumber)
     VALUES (?, ?, ?, ?, ?)
  `;

  return new Promise((resolve, reject) => {
    db.run(
      insertUserQuery,
      [
        newPartner.instituitionName,
        newPartner.cnpj,
        newPartner.email,
        newPartner.password,
        newPartner.cellnumber,
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

export const partnerServices = {
  getPartenerByEmail,
  createPartener,
};
