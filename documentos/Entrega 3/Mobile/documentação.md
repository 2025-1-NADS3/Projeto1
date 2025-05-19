
# 📱 Documentação do Frontend - BankPay Academy - Entrega 3

Aplicativo Android desenvolvido em Java para gerenciamento financeiro estudantil, com funcionalidades como login com token JWT, transações via Pix, compras na cantina, solicitação de serviços e resgate de pontos. Tudo integrado ao backend via API REST.

---

## 📍 Localização do Projeto

```
main/src/Entrega 2/Frontend/BankPay-Academy
Pacote Android: br.com.neonpay.neonpayacademy
```

---

## 📂 Estrutura de Pastas

```
📦 BankPay-Academy
├── app/
│   └── src/
│       └── main/
│           ├── java/br/com/neonpay/neonpayacademy/
│           │   ├── MainActivity.java
│           │   ├── WelcomeActivity.java
│           │   ├── RegisterActivity.java
│           │   ├── LoginActivity.java
│           │   ├── EditProfileActivity.java
│           │   ├── HomeActivity.java
│           │   ├── [diversas telas Pix, Cantina, ASA, Pontos]
│           └── res/
├── bankpay-academy.apk
└── ...
```

---

## 📄 Telas do Aplicativo (Activities)

### 🔐 Autenticação e Acesso
- `MainActivity.java` — Splash screen (inicial)
- `WelcomeActivity.java` — Boas-vindas (Entrar ou Cadastrar)
- `RegisterActivity.java` — Tela de cadastro
- `LoginActivity.java` — Autenticação com JWT

### 🏠 Tela Principal e Perfil
- `HomeActivity.java` — Tela inicial após login
- `EditProfileActivity.java` — Edição e exclusão de perfil
- `ExtractActivity.java` — Extrato de transações
- `FecapServicesActivity.java` — Lista de serviços gerais

### 💸 Módulo Pix
- `PixHomeActivity.java`, `PixTransferActivity.java`, `ConfirmPixPasswordActivity.java`, `ConfirmPixTransferActivity.java`, `PixTransferReceiptActivity.java`
- `PixIntroManageKeysActivity.java`, `PixIntroCreateKeyActivity.java`
  - `PixRegisterCpfKeyActivity.java`
  - `PixRegisterEmailKeyActivity.java`
  - `PixRegisterPhoneKeyActivity.java`
  - `PixRegisterRandomKeyActivity.java`
- `PixKeyListActivity.java`, `PixKeyDetailsActivity.java`, `PixKeyConfirmDeleteActivity.java`, `PixKeySuccessActivity.java`
- Extras: `PixChooseKeyTypeActivity.java`, `PixAddAmountActivity.java`, `PixAddAmountReceiptActivity.java`,
  `PixIntroSendReceiveActivity.java`, `PixConfirmAmountActivity.java`, `PixConfirmationActivity.java`,
  `PixPaymentDetailsActivity.java`, `NoPixKeyActivity.java`

### 🍽️ Módulo Cantina
- `CanteenIntroActivity.java` → `CanteenActivity.java` → `CanteenCartActivity.java` →  
  `CanteenConfirmPasswordActivity.java` → `CanteenReceiptActivity.java`

### 🎓 Módulo ASA
- `AsaIntroActivity.java` → `AsaServiceActivity.java` → `AsaServiceCartActivity.java` →  
  `AsaServiceConfirmPasswordActivity.java` → `AsaServiceReceiptActivity.java`

### 🎁 Módulo de Pontos
- `RedeemPointsIntroActivity.java` → `RedeemPointsActivity.java` →  
  `RedeemPointsConfirmActivity.java` → `RedeemPointsReceiptActivity.java`

---

## 🔐 Armazenamento de Token JWT

- Utiliza `SharedPreferences` para salvar e acessar o token do usuário.
- Classe utilitária: `SharedPrefsHelper.java`

### Métodos:
- `getUsuarioId(context)` → retorna o ID do usuário logado
- `getToken(context)` → retorna o token JWT salvo

Usado principalmente em: `LoginActivity`, `EditProfileActivity`, `PixTransferActivity`, etc.

---

## 🔁 Fluxo de Navegação

### ✅ Fluxo completo entre as telas do app

<p align="center">
  <img src="fluxo_bankpay.png" alt="Fluxograma do BankPay Academy" width="700">
</p>

> ⚠️ Para adicionar essa imagem ao seu README, certifique-se de subir o arquivo `fluxo_bankpay.png` na raiz do seu repositório GitHub.

---

## 📦 APK de Instalação

- O arquivo `bankpay-academy.apk` pode ser instalado diretamente em um dispositivo Android para testes.

---

## 👥 Autores

- [Alexandra Christine](https://www.linkedin.com/in/alexandra-christine-silva-590092257)  
- [Gabrielly Cintra de Jesus](https://www.linkedin.com/in/gabrielly-cintra/)  
- [Hebert dos Reis Esteves](https://linkedin.com/in/hebert-/)  
- [José Bento Almeida Gama](https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/)

---

## 🎓 Projeto Acadêmico

Desenvolvido como parte das disciplinas de **Desenvolvimento Mobile**, **Cloud Native** e **Teste de Software** no curso de Análise e Desenvolvimento de Sistemas da [FECAP](https://www.fecap.br/).
