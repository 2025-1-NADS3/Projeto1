const mysql = require('mysql2');
const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    ssl: { rejectUnauthorized: false }
});

// Testa a conexão com o banco de dados
db.connect((err) => {
    if (err) console.error('Erro ao conectar no MySQL:', err);
    else console.log('Conectado ao MySQL.');
});

module.exports = db;