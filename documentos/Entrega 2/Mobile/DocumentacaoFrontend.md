# 📱 Documentação do Frontend - NeonPay Academy - Entrega 2

Aplicativo Android desenvolvido em Java para gerenciamento de perfil estudantil e transações via Pix, com autenticação JWT e integração com backend via API REST.

📍 **Localização do Projeto:**
```
main/src/Entrega 2/FrontendNeonPay-Academy
Pacote Android: br.com.neonpay.neonpayacademy
```

---

# 📂 Estrutura de Pastas

```
📂NeonPay-Academy
├── 📁app/
│   ├── 📁src/
│   │   ├── 📁main/
│   │   │   ├── 📁java/
│   │   │   │   └── 📁br/com/neonpay/neonpayacademy/
│   │   │   │       ├── 📃MainActivity.java
│   │   │   │       ├── 📃WelcomeActivity.java
│   │   │   │       ├── 📃RegisterActivity.java
│   │   │   │       ├── 📃LoginActivity.java
│   │   │   │       ├── 📃EditProfileActivity.java
│   │   │   │       ├── 📃HomeActivity.java
│   │   │   │       ├── 📃PixHomeActivity.java
│   │   │   │       ├── 📃PixTransferActivity.java
│   │   │   │       ├── 📃ConfirmPixPasswordActivity.java
│   │   │   │       ├── 📃ConfirmPixTransferActivity.java
│   │   │   │       ├── 📃PixTransferReceiptActivity.java
│   │   │   │       └── 📁utils/SharedPrefsHelper.java
```

---

# 📄 Telas do Aplicativo

## 🟣 MainActivity.java (Splash Screen)
- Tela de introdução exibida por 3 segundos.
- Redireciona para: `WelcomeActivity`

## 🟦 WelcomeActivity.java (Tela de Boas-Vindas)
- Escolha entre login e cadastro.
- Botões:
  - `btnCadastrar` → `RegisterActivity`
  - `btnEntrar` → `LoginActivity`

## 🔵 RegisterActivity.java (Tela de Cadastro)
- Campos: Nome, CPF, Data de Nascimento, E-mail, Celular, Senha
- Validações: formato de CPF, celular, domínio do e-mail institucional
- Requisição: `POST` → `/api/cadastro`

## 🟢 LoginActivity.java (Tela de Login)
- Campos: CPF, Senha
- Validação de CPF
- Autenticação via `POST` → `/api/login`
- Armazena token JWT localmente
- Redireciona para: `HomeActivity`

## 🟡 EditProfileActivity.java (Tela de Edição de Perfil)
- Permite atualizar nome, email, telefone, senha
- Funções:
  - Atualizar: `PUT` → `/api/atualizar-perfil`
  - Deletar: `DELETE` → `/api/deletar-perfil`
  - Voltar: WelcomeActivity

## 🏠 HomeActivity.java (Tela Inicial Pós-Login)
- Apresenta saldo, pontos e navegação para área Pix

## 💸 PixHomeActivity.java (Menu Pix)
- Mostra opções de enviar Pix ou ver histórico

## 📤 PixTransferActivity.java (Envio de Pix)
- Campos: valor e chave pix destino
- Validações e envio com token

## 🔐 ConfirmPixPasswordActivity.java (Confirmação de Senha)
- Campo para digitação da senha antes da transferência
- Requisição: POST com senha para `/pix/enviar`

## ✅ ConfirmPixTransferActivity.java (Confirmação de Transferência)
- Tela para revisão dos dados antes da finalização
- Confirmação da transação

## 📄 PixTransferReceiptActivity.java (Comprovante de Transferência)
- Exibe comprovante com valor, chave e hora
- Botão para voltar ao menu Pix

---

# 🔐 Armazenamento de Token
- Utiliza `SharedPreferences` com chave `"TOKEN"`
- Usado em requisições autenticadas (Bearer Token)

---

# 🔁 Fluxo de Navegação

```plaintext
MainActivity
   ↓
WelcomeActivity
   ↙︎            ↘︎
RegisterActivity   LoginActivity
        ↓                ↓
     (volta)        HomeActivity
                          ↓
                ┌──────────────┐
                ↓              ↓
     PixHomeActivity     EditProfileActivity
                ↓              ↑
      PixTransferActivity     (volta)
                ↓
  ConfirmPixPasswordActivity
                ↓
  ConfirmPixTransferActivity
                ↓
  PixTransferReceiptActivity
```

---


---

# 🧠 Classe Utilitária - SharedPrefsHelper.java

Localização:
```
src/FrontEnd/NeonPay-Academy/app/src/main/java/br/com/neonpay/neonpayacademy/utils/SharedPrefsHelper.java
```

Essa classe fornece métodos utilitários para acessar os dados armazenados no `SharedPreferences`, como o ID do usuário e o token de autenticação JWT.

### 🔐 getUsuarioId(Context context)
- Recupera o ID do usuário salvo em `SharedPreferences`.
- Se não houver valor, retorna `-1`.

### 🔑 getToken(Context context)
- Recupera o token JWT salvo localmente após login.
- Se não houver token salvo, retorna `null`.

Esses métodos são amplamente utilizados nas Activities como `LoginActivity`, `EditProfileActivity` e `PixTransferActivity`, para facilitar o uso seguro e centralizado de dados do usuário logado.


## 📌 Autores
- [Alexandra Christine](https://www.linkedin.com/in/alexandra-christine-silva-590092257)  
- [Gabrielly Cintra de Jesus](https://www.linkedin.com/in/gabrielly-cintra/)  
- [Hebert dos Reis Esteves](https://linkedin.com/in/hebert-/)  
- [José Bento Almeida Gama](https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/)
