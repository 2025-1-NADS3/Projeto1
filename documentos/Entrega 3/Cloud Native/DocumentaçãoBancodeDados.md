# 🏦 BankPay Academy

Sistema de gestão de pagamentos, produtos e pontos acadêmicos.

---

## 📁 Estrutura do Projeto

```bash
src/
├── /backend              ← Backend principal (produção)
├── /backend-sandbox      ← Backend do CodeSandbox (desenvolvimento/teste)
└── /frontend             ← Frontend (ex: React, Android, etc)
```

O banco de dados `bankpay_academy` é utilizado em ambos os ambientes de backend (`/backend` e `/backend-sandbox`), e está hospedado na **plataforma Azure**.

---

## 🗃️ Banco de Dados: `bankpay_academy`

- **Local de hospedagem**: Microsoft Azure  
- **Ferramenta de exportação**: phpMyAdmin  
- **Versão do servidor MySQL**: 8.0.40-azure  
- **Codificação padrão**: UTF-8 (`utf8mb4`)  
- **Engine utilizada**: `InnoDB`

---

## 🧩 Tabelas e Estrutura

### 🔐 `usuarios`

Contém dados pessoais e financeiros dos alunos.

| Campo             | Tipo              | Descrição |
|------------------|-------------------|-----------|
| `id`             | int (PK, AI)      | Identificador único. |
| `nome`           | varchar(255)      | Nome completo. |
| `cpf`            | varchar(11)       | CPF (sem formatação). |
| `data_nasc`      | date              | Data de nascimento. |
| `email`          | varchar(255)      | E-mail do aluno. |
| `telefone`       | varchar(15)       | Telefone para contato. |
| `senha`          | varchar(255)      | Senha criptografada. |
| `saldo`          | decimal(10,2)     | Saldo em reais. |
| `pontos`         | int               | Pontos acumulados. |
| `chave_pix`      | varchar(255)      | Chave PIX associada. |
| `tipo_chave_pix` | varchar(20)       | Tipo da chave (email, CPF...). |

🔒 A `chave_pix` é única por usuário.

---

### 💰 `transacoes`

Gerencia entradas e saídas financeiras dos usuários, incluindo trocas por pontos (registradas como `saida` com valor `0.00`).

| Campo        | Tipo                         | Descrição |
|--------------|------------------------------|-----------|
| `id`         | int (PK, AI)                 | ID da transação. |
| `usuario_id` | int (FK)                     | Usuário relacionado. |
| `tipo`       | enum('entrada','saida')      | Tipo da transação. |
| `valor`      | decimal(10,2)                | Valor movimentado. |
| `descricao`  | varchar(255)                 | Motivo ou referência. |
| `chave_pix`  | varchar(100)                 | Chave envolvida na transação. |
| `status`     | enum('pendente','confirmado')| Status da transação. |
| `data`       | timestamp                    | Timestamp do registro. |
| `senha_pedido` | int                        | Código de verificação (autenticação extra). |

📌 As trocas por pontos foram integradas diretamente na tabela de transações com valor `0.00` e descrição iniciada com `Troca por Pontos`.

---

### 🥪 `cantina`

Produtos disponíveis para compra com saldo.

| Campo       | Tipo           | Descrição |
|-------------|----------------|-----------|
| `id`        | int (PK, AI)   | ID do item. |
| `titulo`    | varchar(100)   | Nome do produto. |
| `descricao` | varchar(100)   | Ex: "Unidade", "500ml". |
| `preco`     | decimal(10,2)  | Preço em reais. |
| `imagem`    | varchar(50)    | Caminho para imagem. |

---

### 🧾 `asa_servicos`

Serviços administrativos e estudantis que podem ser solicitados.

| Campo     | Tipo            | Descrição |
|-----------|-----------------|-----------|
| `id`      | int (PK, AI)    | Identificador. |
| `titulo`  | varchar(100)    | Nome do serviço. |
| `preco`   | decimal(10,2)   | Custo do serviço. |
| `imagem`  | varchar(50)     | Nome do arquivo de imagem. |

📌 Ex: 2ª via de documentos, exames, conteúdo programático.

---

### 🎁 `produtos`

Itens que os alunos podem resgatar com pontos acumulados.

| Campo       | Tipo           | Descrição |
|-------------|----------------|-----------|
| `id`        | int (PK, AI)   | Identificador. |
| `titulo`    | varchar(100)   | Nome do produto. |
| `descricao` | varchar(100)   | Detalhe do item. |
| `pontos`    | int            | Quantidade de pontos exigida. |
| `imagem`    | varchar(50)    | Nome da imagem ilustrativa. |

---

### 📊 `historico_pontos`

Controla os pontos gastos mensalmente.

| Campo          | Tipo         | Descrição |
|----------------|--------------|-----------|
| `id`           | int (PK, AI) | ID do registro. |
| `id_usuario`   | int          | ID do usuário. |
| `mes`          | date         | Mês da movimentação. |
| `pontos_usados`| int          | Quantidade de pontos usados. |

---

## ✅ Considerações Técnicas

- Todas as tabelas usam `InnoDB`, com suporte a integridade referencial.
- A tabela `transacoes` foi expandida para registrar também trocas por pontos (valor `0.00`).
- Todas as imagens são armazenadas por nome (ex: `img_moletom`), devendo ser resolvidas pelo frontend.

---

## ☁️ Hospedagem

O banco de dados está hospedado na **Azure Database for MySQL**, permitindo alta disponibilidade e escalabilidade para ambientes de produção.

---

