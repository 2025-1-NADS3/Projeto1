# 🖥️ Backend - BankPay

Este projeto representa o backend do sistema **BankPay**, desenvolvido com **Node.js** e **Express**. Utilizamos o **PM2** como gerenciador de processos para manter o servidor em execução contínua, mesmo após o terminal ser encerrado ou o sistema reiniciado.

---

## 📁 Estrutura do Projeto

```
/Backend
├── node_modules/
├── server.js
├── package.json
├── README.md
└── /Projeto1/imagens
    ├── imagem 1.jpg
    ├── imagem 2.jpg
    ├── imagem 3.jpg
    └── imagem 4.jpg
```

---

## ⚙️ Passos para executar o servidor com PM2

### 1️⃣ Instalar as dependências do projeto

```bash
npm install
```

---

### 2️⃣ Instalar o PM2 globalmente

```bash
npm install -g pm2
```

---

### 3️⃣ Iniciar o servidor com PM2

Dentro da pasta onde está o `server.js`, execute:

```bash
pm2 start server.js --name bankpay-backend
```

📸 *Exemplo de saída:*  
![Start do servidor com PM2](./Projeto1/imagens/imagem%201.jpg)

---

### 4️⃣ Verificar se o servidor está rodando

Execute:

```bash
pm2 list
```

📸 *Exemplo de listagem com status "online":*  
![Listagem PM2](./Projeto1/imagens/imagem%202.jpg)

---

### 5️⃣ Salvar o estado atual do PM2

Para garantir que o processo seja restaurado após reinicializações:

```bash
pm2 save
```

📸 *Confirmação do comando `pm2 save`:*  
![PM2 Save](./Projeto1/imagens/imagem%203.jpg)

---

### 6️⃣ Simular reinício e restaurar processos

Para testar se o PM2 irá restaurar o servidor automaticamente:

```bash
pm2 kill
pm2 resurrect
```

📸 *Resultado após o `resurrect` com processo restaurado:*  
![PM2 Resurrect](./Projeto1/imagens/imagem%204.jpg)

---

## ✅ Considerações Finais

A utilização do **PM2** permite:

- Manter o backend rodando mesmo após fechar o terminal.
- Restaurar os processos salvos com facilidade.
- Acompanhar uso de CPU, memória e status da aplicação.
- Gerenciar múltiplas aplicações Node.js com simplicidade.

Essa prática é ideal para ambientes de produção e demonstra domínio em **deploy**, **resiliência de serviços** e **automação de processos Node.js**.

---

> 💡 Dica: Para visualizar logs da aplicação em tempo real, utilize:
> ```bash
> pm2 logs
> ```
