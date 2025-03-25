## 💳 NeonPay Academy — Backend
Backend do aplicativo NeonPay Academy, um sistema de pagamentos acadêmicos que permite login, cadastro, controle de perfil e integração com banco de dados MySQL, utilizando Node.js com autenticação JWT.
## 🚀 Tecnologias Utilizadas
Node.js + Express.js

MySQL (mysql2)

JWT (autenticação)

Bcrypt (criptografia de senhas)

Dotenv (variáveis de ambiente)

CORS (acesso entre domínios)

## 📂Estrutura de Pasta 

NeonPay-Academy/
├── config/
│   └── db.js
├── controllers/
│   └── userController.js
├── middlewares/
│   └── autenticarToken.js
├── routes/
│   └── userRoutes.js
├── server.js
├── .env
├── package.json
└── package-lock.json

## ⚙️ Variáveis de Ambiente (.env)
```
PORT=3000
DB_HOST=localhost
DB_USER=usuario
DB_PASSWORD=senha
DB_NAME=neonpay_academy
JWT_SECRET=sua_chave_secreta
```
## 🛠️ Configuração do Banco de Dados (config/db.js)
```
const mysql = require('mysql2');

const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    ssl: { rejectUnauthorized: false }
});

db.connect((err) => {
    if (err) console.error('Erro ao conectar no MySQL:', err);
    else console.log('Conectado ao MySQL.');
});

module.exports = db;
```
## 🔐 Middleware de Autenticação JWT (middlewares/autenticarToken.js)
```
const jwt = require('jsonwebtoken');
require('dotenv').config();

const JWT_SECRET = process.env.JWT_SECRET;

function autenticarToken(req, res, next) {
    const token = req.headers['authorization']?.split(' ')[1];

    if (!token) return res.status(401).json({ erro: 'Token não fornecido' });

    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) return res.status(401).json({ erro: 'Token inválido ou expirado.' });

        req.user = decoded;
        next();
    });
}

module.exports = autenticarToken;
```
## 🧠 Lógica de Usuário (controllers/userController.js)
- Login com CPF e senha
```
POST /api/login
```
Retorna token JWT
- Cadastro de novo usuário
```
POST /api/cadastro
```
Senha criptografada com Bcrypt

- Atualiza nome, email e senha 
  
```
PUT /api/atualizar-perfil
```
Necessário estar autenticado (via JWT)
- Remove o usuário do sistema
```
DELETE /api/deletar-perfil
```
Necessário estar autenticado
- Retorna informações do usuário autenticado
```
GET /api/perfil
```
## 🌐 Rotas HTTP (routes/userRoutes.js)
```
router.post('/login', login);
router.post('/cadastro', register);
router.put('/atualizar-perfil', autenticarToken, atualizarPerfil);
router.delete('/deletar-perfil', autenticarToken, deletarPerfil);
router.get('/perfil', autenticarToken, getPerfil);
```
## 🖥️ Inicialização do Servidor (server.js)
```
const express = require('express');
const cors = require('cors');
require('dotenv').config();

const db = require('./config/db');
const userRoutes = require('./routes/userRoutes');

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());
app.use(cors());

app.use('/api', userRoutes);

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});
```
## 📦 Dependências
```
"dependencies": {
  "bcrypt": "^5.1.1",
  "cors": "^2.8.5",
  "dotenv": "^16.4.7",
  "express": "^4.21.2",
  "jsonwebtoken": "^9.0.2",
  "mysql2": "^3.14.0"
}
```
## ✅ Segurança e Boas Práticas
Senhas criptografadas com Bcrypt (salt automático)

Token JWT com expiração de 2 horas

Middleware de autenticação em rotas protegidas

Validação de dados obrigatórios em todos os endpoints

## 📌 Autores
Desenvolvido por:
Alexandra Christine.
Contato: LinkedIn | GitHub
Gabrielly Cintra
Contato: LinkedIn | GitHub
Hebert Esteves
Contato: LinkedIn | GitHub
José Bento
Contato: LinkedIn | GitHub


