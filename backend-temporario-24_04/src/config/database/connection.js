import sqlite3 from 'sqlite3';
const sqlite = sqlite3.verbose();
const DBPATH = "./src/config/database/dbprojeto.db";

export const db = new sqlite.Database(DBPATH);