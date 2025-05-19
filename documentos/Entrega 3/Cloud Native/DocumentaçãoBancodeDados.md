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

O banco de dados `bankpay_academy` é utilizado em ambos os ambientes de backend (`/backend` e `/backend-sandbox`).

---

## 🗃️ Banco de Dados: `bankpay_academy`

- **Ferramenta de exportação**: phpMyAdmin 5.2.1  
- **Versão do servidor MySQL**: 9.1.0  
- **Codificação padrão**: UTF-8 (`utf8mb4`)  
- **Engine padrão**: `MyISAM` (exceto `usuarios`, que usa `InnoDB`)

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

Gerencia entradas e saídas financeiras dos usuários.

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

### 🔄 `historico_trocas`

Registra todas as trocas de pontos por produtos.

| Campo        | Tipo          | Descrição |
|--------------|---------------|-----------|
| `id`         | int (PK, AI)  | ID da troca. |
| `usuario_id` | int (FK)      | Quem realizou a troca. |
| `produto_id` | int (FK)      | Produto resgatado. |
| `data`       | datetime      | Data da troca. |

---

## ✅ Considerações Técnicas

- O uso de `MyISAM` impede uso de *foreign keys reais* — considerar migrar para `InnoDB` para integridade referencial.
- Tabelas de relacionamento (ex: `historico_trocas`) poderiam ser mais robustas com `FOREIGN KEY`.
- Todas as imagens são armazenadas por nome (ex: `img_moletom`), devendo ser resolvidas pelo frontend.

---
