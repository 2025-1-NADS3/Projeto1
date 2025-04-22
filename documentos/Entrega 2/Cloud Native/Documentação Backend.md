# 🖥️ Backend - NeonPay Academy

Este é o backend do projeto **NeonPay Academy**, desenvolvido como parte do Projeto 1 da disciplina de Desenvolvimento Web Full Stack da FECAP. Ele fornece uma API RESTful para autenticação de usuários, cadastro, envio de transações Pix e gerenciamento de perfil.

---

## 📁 Estrutura de Pastas

```
Backend/
├── config/
│   └── db.js                      # Configuração da conexão com o banco de dados MySQL
├── controllers/
│   ├── pixController.js          # Lógica de envio e recebimento de Pix
│   └── userController.js         # Lógica de cadastro, login, perfil e atualizações
├── middlewares/
│   └── auth.js                   # Middleware de autenticação com JWT
├── routes/
│   ├── pixRoutes.js              # Rotas relacionadas ao Pix
│   └── userRoutes.js             # Rotas relacionadas a usuário (login, cadastro, perfil)
├── uploads/
│   └── (imagens salvas via Multer)
├── .env                          # Variáveis de ambiente (não enviado ao repositório)
├── .gitignore                    # Ignora arquivos sensíveis e node_modules
├── package.json                  # Lista de dependências e scripts
├── package-lock.json             # Registro de versões exatas das dependências
└── server.js                     # Arquivo principal que inicializa o servidor
```

---

## ⚙️ Tecnologias Utilizadas

- Node.js
- Express.js
- MongoDB com Mongoose
- JWT (JSON Web Token)
- Bcrypt (criptografia de senhas)
- Dotenv (variáveis de ambiente)
- CORS

---

## 🔐 Autenticação

- **Token JWT**: Gerado no login e enviado no header `Authorization: Bearer <token>` para rotas protegidas.
- **Middleware de Autenticação**: Verifica a validade do token em rotas que requerem autenticação.

---

## 📄 Endpoints da API

### 🔸 POST `/api/cadastro`

- **Descrição**: Cadastra um novo usuário.
- **Corpo da Requisição**:
  ```json
  {
    "nome": "João Silva",
    "cpf": "12345678900",
    "data_nascimento": "1990-01-01",
    "email": "joao@edu.fecap.br",
    "telefone": "11999999999",
    "senha": "senhaSegura123"
  }
## Validações:

CPF válido e único

E-mail com domínio @edu.fecap.br

Telefone com 10 ou 11 dígitos numéricos

Data de nascimento no formato yyyy-MM-dd

🔸 POST /api/login
Descrição: Autentica o usuário e retorna um token JWT.

Corpo da Requisição:
```
{
  "cpf": "12345678900",
  "senha": "senhaSegura123"
}
```
Resposta:

```
{
  "token": "jwt.token.aqui"
}
```
🔸 GET /api/perfil
Descrição: Retorna os dados do perfil do usuário autenticado.

Headers:

```
Authorization: Bearer <token>

```
🔸 PUT /api/atualizar-perfil
Descrição: Atualiza os dados do perfil do usuário autenticado.

Headers:
```
Authorization: Bearer <token>
```
Corpo da Requisição:
```
{
  "nome": "João da Silva",
  "email": "joao.silva@edu.fecap.br",
  "telefone": "11988888888",
  "senha": "novaSenhaSegura123"
}
```
🔸 DELETE /api/deletar-perfil
Descrição: Exclui o perfil do usuário autenticado.

Headers:
```
Authorization: Bearer <token>
```
🔸 POST /api/pix/enviar
Descrição: Realiza uma transferência via Pix.

Headers:
```
Authorization: Bearer <token>
```
Corpo da Requisição:

```
{
  "valor": 100.00,
  "chave_pix_destino": "destino@pix.com",
  "senha": "senhaSegura123"
}
```
Validações:

Verifica se a senha está correta

Verifica se há saldo suficiente

🔄 Fluxo de Requisições
```
[POST] /api/cadastro
   ↓
[POST] /api/login → retorna token JWT
   ↓
[GET] /api/perfil → requer token
   ↓
[PUT] /api/atualizar-perfil → requer token
   ↓
[POST] /api/pix/enviar → requer token
   ↓
[DELETE] /api/deletar-perfil → requer token
```
▶️ Como Executar o Backend
1. Clonar o repositório:
```
git clone https://github.com/2025-1-NADS3/Projeto1
```
2. Navegar até o diretório do backend:
```
cd Projeto1/src/Entrega\ 2/Backend
```
3. Instalar as dependências:
```
npm install
Configurar as variáveis de ambiente:
```
4. Criar um arquivo .env com as seguintes variáveis:
```
PORT=3000
MONGO_URI=seu_mongo_uri_aqui
JWT_SECRET=sua_chave_secreta_aqui
```
Iniciar o servidor:
```
npm start
```

📌 Observações
Certifique-se de que o MongoDB esteja em execução e acessível através da URI fornecida.

Utilize ferramentas como Postman ou Insomnia para testar os endpoints da API.

O frontend do projeto está localizado em:
```
src/Entrega 2/Frontend/NeonPay-Academy
```

## 😁 Autores
Desenvolvido por:
<a href="https://www.linkedin.com/in/alexandra-christine-silva-590092257">Alexandra Christine </a>,<a href="https://www.linkedin.com/in/gabrielly-cintra/">Gabrielly Cintra de Jesus	</a>, <a href="https://linkedin.com/in/hebert-/">Hebert dos Reis Esteves	</a>, e <a href="https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/" >José Bento Almeida Gama </a>.

