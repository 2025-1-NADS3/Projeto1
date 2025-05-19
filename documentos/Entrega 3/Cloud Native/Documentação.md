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
![imagem 1](https://github.com/user-attachments/assets/02c0d9ba-52f0-47a1-80f9-f4d87148df81)


---

### 4️⃣ Verificar se o servidor está rodando

Execute:

```bash
pm2 list
```

📸 *Exemplo de listagem com status "online":*  
![imagem 2](https://github.com/user-attachments/assets/7d6c03c9-264a-4625-8f65-316ba08e39b8)


---

### 5️⃣ Salvar o estado atual do PM2

Para garantir que o processo seja restaurado após reinicializações:

```bash
pm2 save
```

📸 *Confirmação do comando `pm2 save`:*  
![imagem 3](https://github.com/user-attachments/assets/8dd0edac-4d53-40a6-b955-87947485b1b3)


---

### 6️⃣ Simular reinício e restaurar processos

Para testar se o PM2 irá restaurar o servidor automaticamente:

```bash
pm2 kill
pm2 resurrect
```

📸 *Resultado após o `resurrect` com processo restaurado:*  
![imagem 4](https://github.com/user-attachments/assets/1a21174f-ce61-4257-9886-c2e67448f13c)


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
