import sqlite3 from 'sqlite3';

/**
 * Configuração do banco de dados SQLite.
 */
const sqlite = sqlite3.verbose();
const DBPATH = "./src/config/database/dbprojetoFormaFinal.db";

/**
 * Instância do banco de dados SQLite.
 * @type {sqlite3.Database}
 */
export const db = new sqlite.Database(DBPATH, (err) => {
  if (err) {
    console.error(err.message);
  }
  console.log('Conectado ao SQLite.');
});

/** Habilitar chaves estrangeiras */
db.run("PRAGMA foreign_keys = ON;", [], function(err) {
  if (err) {
    console.error(err.message);
  }
});