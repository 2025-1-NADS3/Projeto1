# 💳 NeonPay Academy — Backend

Backend do NeonPay Academy, um sistema de pagamentos para estudantes com funcionalidades de login, cadastro, PIX, pontuação por uso e resgate de prêmios. Utiliza Node.js com MySQL, autenticação JWT e criptografia de senhas.

## 🚀 Tecnologias Utilizadas

- Node.js + Express.js
- MySQL (mysql2/promise)
- JWT (autenticação)
- Bcrypt (criptografia de senhas)
- Dotenv (variáveis de ambiente)
- CORS (acesso entre domínios)

## 📂 Estrutura de Pastas

```
📂src
├── 📂config
│   └── 📃db.js
├── 📂controllers
│   ├── 📃pixController.js
│   └── 📃userController.js
├── 📂middlewares
│   └── 📃autenticarToken.js
├── 📂routes
│   ├── 📃pixRoutes.js
│   └── 📃userRoutes.js
├── 📃server.js
├── 📃package.json
└── 📃package-lock.json
```

## ⚙️ Variáveis de Ambiente (.env)

```
PORT=3000
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=suasenha
DB_NAME=neonpay
JWT_SECRET=sua_chave_super_secreta
```

## 🛠️ Configuração do Banco (config/db.js)

```js
import mysql from 'mysql2/promise';
import dotenv from 'dotenv';
dotenv.config();

const db = mysql.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  ssl: { rejectUnauthorized: false }
});

export default db;
```

## 🔐 Middleware de Autenticação (middlewares/autenticarToken.js)

- Verifica se o token JWT está presente e válido
- Usa `promisify` para validar tokens de forma assíncrona

## 👤 Controle de Usuários (controllers/userController.js)

- `POST /login`: autentica o usuário e retorna um JWT
- `POST /cadastro`: registra novo usuário com senha criptografada
- `PUT /atualizar-perfil`: atualiza dados pessoais e senha (opcional)
- `DELETE /deletar-perfil`: remove usuário autenticado
- `GET /perfil`: retorna informações do usuário logado
- `POST /trocar-pontos`: permite troca de pontos por produtos
- `GET /historico-pontos`: retorna resumo mensal do uso de pontos

## 💸 Controle de Pix (controllers/pixController.js)

- `POST /pix/gerar-cobranca`: simula a geração de QR Code para depósito
- `POST /pix/webhook`: simula a confirmação de pagamento e atualização do saldo
- `POST /pix/enviar`: envia PIX com validação de senha e saldo, e gera pontos
- `GET /pix/saldo/:id`: retorna o saldo do usuário
- `GET /pix/pontos/:id`: retorna os pontos acumulados

## 🌐 Rotas de Usuário (routes/userRoutes.js)

```js
router.post('/login', login);
router.post('/cadastro', register);
router.put('/atualizar-perfil', autenticarToken, atualizarPerfil);
router.delete('/deletar-perfil', autenticarToken, deletarPerfil);
router.get('/perfil', autenticarToken, getPerfil);
router.post('/trocar-pontos', autenticarToken, trocarPontosPorProduto);
router.get('/historico-pontos', autenticarToken, listarHistoricoPontos);
```

## 💳 Rotas de Pix (routes/pixRoutes.js)

```js
router.post("/gerar-cobranca", gerarCobranca);
router.post("/webhook", webhook);
router.post("/enviar", enviarPix);
router.get("/saldo/:id", consultarSaldo);
router.get("/pontos/:id", consultarPontos);
```

## 🚀 Inicialização do Servidor (server.js)

```js
app.use('/api', userRoutes);
app.use('/pix', pixRoutes);

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});
```

## 📦 Dependências (package.json)

```json
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

- Autenticação com JWT (2h de validade)
- Criptografia de senha com Bcrypt
- Proteção de rotas via middleware
- Validação de dados em todas as requisições

## 👥 Autores

Desenvolvido por:
- [Alexandra Christine](https://www.linkedin.com/in/alexandra-christine-silva-590092257)
- [Gabrielly Cintra de Jesus](https://www.linkedin.com/in/gabrielly-cintra/)
- [Hebert dos Reis Esteves](https://linkedin.com/in/hebert-/)
- [José Bento Almeida Gama](https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256)
