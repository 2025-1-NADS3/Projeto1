# 🏦 BankPay Academy

Sistema de pagamentos e benefícios estudantis integrados à plataforma da instituição.

## 📁 Banco de Dados

Nome: `bankpay_academy`  
Gerado via: phpMyAdmin 5.2.1  
Codificação: UTF-8 (utf8mb4)

---

## 📄 Tabelas

### 🔐 `usuarios`
Armazena informações dos usuários do sistema, incluindo dados pessoais, autenticação, saldo e pontos.

### 💰 `transacoes`
Histórico financeiro com PIX, entradas e saídas, associadas aos usuários.

### 🥪 `cantina`
Tabela com os produtos disponíveis para compra na cantina física da instituição.

### 🧾 `asa_servicos`
Serviços administrativos e estudantis oferecidos pelo ASA com cobrança via saldo.

### 🎁 `produtos`
Itens da atlética que podem ser adquiridos com pontos acumulados.

### 📊 `historico_pontos`
Registros mensais de pontos utilizados por cada aluno.

### 🔄 `historico_trocas`
Rastreamento de quais produtos foram trocados por pontos, por quais usuários e quando.

---

## 🛠️ Tecnologias

- MySQL 9.1.0
- PHP 8.3.14
- phpMyAdmin 5.2.1

---

## 🧠 Observações

- O sistema permite que estudantes acumulem pontos com base em interações financeiras.
- Esses pontos podem ser trocados por produtos ou serviços.
- A plataforma é extensível para dashboards e relatórios acadêmicos e financeiros.

---

## 📷 Imagens

Os campos `imagem` em diversas tabelas armazenam nomes de arquivos que devem ser referenciados no frontend.

---

## 📌 Sugestão de Expansão

- Implementar foreign keys reais para consistência referencial.
- Adicionar logs de auditoria e histórico de alterações.
- Normalizar melhor as tabelas `transacoes` e `produtos`.

---

## 📫 Contato

Para dúvidas ou sugestões, entre em contato com a equipe de desenvolvimento.

