import { db } from "../config/database/connection.js";

/** Muda a senha do usuário de acordo com o tipo do mesmo */
const changeUserPasswordByEmail = async (userType, userEmail, newPassword) => {
  const sql = `UPDATE ${userType} SET password = ? WHERE email = ?`;

  return new Promise((resolve, reject) => {
    db.run(sql, [newPassword, userEmail], function (err) {
      if (err) {
        reject(err);
      } else {
        resolve({ changes: this.changes });
      }
    });
  });
};

export const allUserTypesServices = {
  changeUserPasswordByEmail
}