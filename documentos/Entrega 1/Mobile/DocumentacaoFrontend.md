# 📱 Documentação do Frontend - NeonPay Academy
O aplicativo mobile NeonPay Academy está localizado em:
```
src/FrontEnd/NeonPay-Academy
Pacote Android: br.com.neonpay.neonpayacademy
```
# 📄 Telas do Aplicativo
## 🟣 MainActivity.java (Splash Screen)

Função: Tela de abertura do app, exibida por 3 segundos antes de redirecionar o usuário.

Destino: WelcomeActivity

Observações: Uso de Handler e Looper para temporização da transição.

## 🟦 WelcomeActivity.java (Tela de Boas-Vindas)
Botões:

btnRegister: Redireciona para RegisterActivity

btnLogin: Redireciona para LoginActivity

Objetivo: Servir como hub de entrada para cadastro ou login.

## 🔵 RegisterActivity.java (Tela de Cadastro)
Campos: Nome, CPF, Data de Nascimento, E-mail, Celular, Senha

Validações:

CPF: Verificação de dígitos e formatação

E-mail: Domínio institucional @edu.

Celular: Apenas números com 10 ou 11 dígitos

Data de Nascimento: Conversão para o formato yyyy-MM-dd

Requisição: POST para /api/cadastro

## 🟢 LoginActivity.java (Tela de Login)
Campos: CPF, Senha

Validações:

CPF (mesma lógica de validação do cadastro)

Requisição: POST para /api/login

Ações pós-login:

Armazena o token em SharedPreferences

Redireciona para EditProfileActivity

## 🟡 EditProfileActivity.java (Tela de Perfil)
Campos Editáveis: Nome, Email, Telefone, Senha

Botões:

btnUpdate: Atualiza os dados do usuário

btnDelete: Exclui o perfil com alerta de confirmação

imgVoltar: Retorna à tela de boas-vindas

## ⚙️ Funções:
```
carregarPerfil(): Requisição GET para /api/perfil

atualizarPerfil(): Requisição PUT para /api/atualizar-perfil

deletarConta(): Requisição DELETE para /api/deletar-perfil

Token: Todas as requisições exigem token JWT nos headers (Bearer <token>)
```
# 🔐 Armazenamento Seguro de Token
<br>Os tokens JWT são armazenados em SharedPreferences sob a chave "TOKEN".</br>
<br>Utilizados em todas as requisições autenticadas no app.</br>

# 🔁 Fluxo de Navegação
    <br>A[MainActivity - Splash Screen]</br>
    <br>B[WelcomeActivity]</br>
    <br>C[RegisterActivity]</br>
   <br> D[LoginActivity]</br>
   <br> E[EditProfileActivity]</br>

    A --> B
    B --> C
    B --> D
    D --> E
    C --> D
    E --> B
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

