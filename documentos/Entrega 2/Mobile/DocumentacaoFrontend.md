# 📱 Documentação do Frontend - NeonPay Academy

O aplicativo mobile **NeonPay Academy** está localizado em:

---

# 📄 Telas do Aplicativo

## 🟣 SplashActivity.java (Splash Screen)

- **Função**: Tela de abertura do app, exibida por 3 segundos.
- **Destino**: Redireciona automaticamente para `BoasVindasActivity`.
- **Detalhes Técnicos**: Utiliza `Handler` e `Looper` para controle do tempo.

---

## 🟦 BoasVindasActivity.java (Tela de Boas-Vindas)

- **Função**: Tela de boas-vindas com entrada para login ou cadastro.
- **Botões**:
  - `btnCadastrar` → Redireciona para `CadastroActivity`
  - `btnEntrar` → Redireciona para `LoginActivity`

---

## 🔵 CadastroActivity.java (Tela de Cadastro)

- **Campos**: Nome, CPF, Data de Nascimento, E-mail, Celular, Senha
- **Validações**:
  - **CPF**: Formato válido e 11 dígitos
  - **E-mail**: Deve conter domínio institucional `@edu.fecap.br`
  - **Celular**: 10 ou 11 dígitos numéricos
  - **Data de nascimento**: Convertida para `yyyy-MM-dd`
- **Requisição**:  
  ```
  POST /api/cadastro
  Content-Type: application/json
  ```
  
Envia os dados do formulário em JSON

Fluxo: Cadastro bem-sucedido → LoginActivity

🟢 LoginActivity.java (Tela de Login)
Campos: CPF, Senha

Validações:

CPF formatado e válido
```
Requisição:
POST /api/login
Content-Type: application/json
```

Resposta retorna um token JWT

Após Login:

Armazena o token em SharedPreferences com a chave "TOKEN"

Redireciona para HomeActivity

🏠 HomeActivity.java (Tela Principal)
Função: Tela principal pós-login com saldo, pontos e gráfico de movimentações.

Componentes:

Gráfico de barras (MPAndroidChart)

Botão de envio Pix → TransferirPixActivity
```
Requisição de saldo/pontos:
GET /api/saldo
GET /api/pontos
Headers: Authorization: Bearer <TOKEN>
```
💸 TransferirPixActivity.java (Tela de Envio de Pix)
Campos: Valor, Chave Pix, Senha

Validações:

Verifica se a senha digitada está correta

Verifica se há saldo suficiente

Requisição:
```
POST /api/pix/enviar
Headers: Authorization: Bearer <TOKEN>
Body: { usuario_id, valor, chave_pix_destino, senha }
```
Resposta:

Em caso de sucesso: Redireciona para ConfirmPixTransferActivity

Em caso de falha: Mensagem "Senha incorreta"

🧾 ConfirmPixTransferActivity.java (Comprovante de Transação)
Função: Exibe o comprovante com os dados da transação:

Valor enviado

Chave Pix

Status e hora da transação

Fluxo: Pode retornar para HomeActivity

🔐 Armazenamento Seguro de Token
Os tokens JWT são armazenados localmente em SharedPreferences sob a chave:
```
"TOKEN"
```
Todas as requisições autenticadas enviam o token no header:
```
Authorization: Bearer <TOKEN>
```

🔁 Fluxo de Navegação
SplashActivity
     ↓
BoasVindasActivity
     ├──→ CadastroActivity ──→ LoginActivity
     └──→ LoginActivity ──→ HomeActivity ──→ TransferirPixActivity ──→ ConfirmPixTransferActivity

     
## 😁 Autores
Desenvolvido por:
<a href="https://www.linkedin.com/in/alexandra-christine-silva-590092257">Alexandra Christine </a>,<a href="https://www.linkedin.com/in/gabrielly-cintra/">Gabrielly Cintra de Jesus	</a>, <a href="https://linkedin.com/in/hebert-/">Hebert dos Reis Esteves	</a>, <a href="https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/">José Bento Almeida Gama </a>.



