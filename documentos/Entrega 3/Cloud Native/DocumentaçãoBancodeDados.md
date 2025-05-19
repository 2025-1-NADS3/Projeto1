# 🏦 BankPay Academy

Sistema de gestão de pagamentos, produtos e pontos acadêmicos.

## 📁 Estrutura do Projeto

```bash
src/
├── /backend              ← Backend principal (produção)
├── /backend-sandbox      ← Backend do CodeSandbox (desenvolvimento/teste)
└── /frontend             ← Frontend (ex: React, Android, etc)
```

## 🗃️ Banco de Dados: `bankpay_academy`

Gerenciado em MySQL 9.1.0 com codificação `utf8mb4`.

### Tabelas principais:

- `usuarios` → Informações dos alunos
- `transacoes` → Histórico financeiro com PIX
- `cantina` → Itens disponíveis para compra
- `asa_servicos` → Serviços acadêmicos como 2ª via de documentos
- `produtos` → Itens da atlética trocáveis por pontos
- `historico_pontos` → Controle mensal de pontos gastos
- `historico_trocas` → Registro das trocas de pontos por produtos

## 🧠 Funcionalidades Suportadas

- Pagamentos via PIX
- Registro de saldo e pontos
- Troca de pontos por prêmios
- Compra de produtos na cantina
- Solicitação de serviços da secretaria/ASA

## 📌 Observações Técnicas

- Algumas tabelas usam `MyISAM` (sem integridade referencial).
- Todas as imagens são chamadas por nome do arquivo.
- É recomendado migrar tabelas relacionais para `InnoDB`.

## 📫 Contato

Para suporte ou contribuições, entre em contato com a equipe de desenvolvimento.
