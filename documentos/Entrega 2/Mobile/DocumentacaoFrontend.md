# 📱 Documentação do Frontend - NeonPay Academy - Entrega 2

O aplicativo mobile **NeonPay Academy** foi desenvolvido em Android Studio com linguagem Java. Ele permite o cadastro, login e edição de perfil de usuários acadêmicos, com comunicação a uma API REST.

📍 **Localização do Projeto:**
```
src/FrontEnd/NeonPay-Academy
Pacote Android: br.com.neonpay.neonpayacademy
```

---

# 📂 Estrutura de Pastas

```
📂NeonPay-Academy
├── 📁.idea/
├── 📁app/
│   ├── 📁src/
│   │   ├── 📁main/
│   │   │   ├── 📁java/
│   │   │   │   └── 📁br/com/neonpay/neonpayacademy/
│   │   │   │       ├── 📃CadastroActivity.java
│   │   │   │       ├── 📃EditProfileActivity.java
│   │   │   │       ├── 📃LoginActivity.java
│   │   │   │       ├── 📃MainActivity.java
│   │   │   │       └── 📃WelcomeActivity.java
│   │   │   ├── 📁res/
│   │   │   │   ├── 📁layout/
│   │   │   │   └── 📁drawable/
│   │   │   │   └── 📁values/
│   └── 📃AndroidManifest.xml
└── 📃build.gradle
```

---

# 📄 Telas do Aplicativo

## 🟣 MainActivity.java (Splash Screen)

- **Função:** Tela de introdução do app.
- **Destino:** Redireciona automaticamente para `WelcomeActivity` após 3 segundos.
- **Implementação Técnica:** Uso de `Handler` e `Looper`.

---

## 🟦 WelcomeActivity.java (Tela de Boas-Vindas)

- **Função:** Tela inicial com opções para login ou cadastro.
- **Botões:**
  - `btnEntrar` → Redireciona para `LoginActivity`
  - `btnCadastrar` → Redireciona para `CadastroActivity`

---

## 🔵 CadastroActivity.java (Tela de Cadastro)

- **Campos:** Nome, CPF, Data de Nascimento, E-mail, Celular, Senha
- **Validações:**
  - CPF: Apenas numérico e com 11 dígitos
  - E-mail: Deve conter domínio institucional `@edu.fecap.br`
  - Celular: 10 ou 11 dígitos
  - Data de Nascimento: Conversão para o formato `yyyy-MM-dd`
- **Requisição:** `POST` para `/api/cadastro`

---

## 🟢 LoginActivity.java (Tela de Login)

- **Campos:** CPF, Senha
- **Validações:** CPF válido e senha obrigatória
- **Requisição:** `POST` para `/api/login`
- **Ações:**
  - Armazena `token JWT` em `SharedPreferences`
  - Redireciona para `EditProfileActivity`

---

## 🟡 EditProfileActivity.java (Tela de Perfil)

- **Campos Editáveis:** Nome, E-mail, Celular, Senha
- **Botões:**
  - `btnUpdate`: Atualiza dados do usuário
  - `btnDelete`: Exclui o perfil com alerta de confirmação
  - `imgVoltar`: Volta para tela de boas-vindas

---

# ⚙️ Funções de Requisição

```java
carregarPerfil()           // GET para /api/perfil
atualizarPerfil()          // PUT para /api/atualizar-perfil
deletarConta()             // DELETE para /api/deletar-perfil
```

Todas as funções utilizam o token JWT no cabeçalho da requisição:  
`Authorization: Bearer <token>`

---

# 🔐 Armazenamento Seguro de Token

- Tokens JWT são armazenados localmente via `SharedPreferences` na chave `"TOKEN"`.
- Utilizados em todas as chamadas autenticadas da aplicação.

---

# 🔁 Fluxo de Navegação

```plaintext
MainActivity (Splash)
     ↓
WelcomeActivity
     ↙︎         ↘︎
LoginActivity  CadastroActivity
     ↓              ↓
EditProfileActivity ←──────
```

---

## 📌 Autores
- <a href="https://www.linkedin.com/in/alexandra-christine-silva-590092257">Alexandra Christine</a>  
- <a href="https://www.linkedin.com/in/gabrielly-cintra/">Gabrielly Cintra de Jesus</a>  
- <a href="https://linkedin.com/in/hebert-/">Hebert dos Reis Esteves</a>  
- <a href="https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/">José Bento Almeida Gama</a>
