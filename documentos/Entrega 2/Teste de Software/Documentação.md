
## 🧪 Detalhamento dos Testes
tests/
├── unitarios/
│   ├── cadastro.test.js
│   ├── consultaPontos.test.js
│   ├── login.test.js
│   ├── trocaPontos.test.js
│   └── cargaUnitario.test.js
│
├── integracao/
│   ├── pix.test.js
│   ├── pixSenhaIncorreta.test.js
│   └── cargaIntegracao.test.js
│
├── jest.config.js
├── package.json
└── package-lock.json

### Testes Unitários

- **`cadastro_unitario_test.js`**: Verifica o processo de cadastro de usuários, assegurando que os dados são armazenados corretamente e que as validações estão funcionando conforme o esperado.

- **`consultaPontos_unitario_test.js`**: Testa a funcionalidade de consulta de pontos, garantindo que o sistema retorna o saldo correto para diferentes cenários.

- **`login_unitario_test.js`**: Avalia o processo de autenticação de usuários, incluindo casos de sucesso e falha (e.g., senha incorreta).

- **`trocaPontos_unitario_test.js`**: Testa a conversão de pontos por recompensas, verificando se a lógica de dedução e validação de pontos está correta.

- **`testes_carga_unitario.test.js`**: Executa testes de carga em funções individuais para avaliar o desempenho sob diferentes volumes de dados.

### Testes de Integração

- **`pix.integration.test.js`**: Simula transações via PIX, testando a integração entre os módulos de transferência e autenticação.

- **`pix.senha.incorreta.test.js`**: Verifica o comportamento do sistema quando uma transação PIX é tentada com senha incorreta, assegurando que a transação é bloqueada e que mensagens de erro apropriadas são exibidas.

- **`testes_carga_integracao.test.js`**: Avalia o desempenho do sistema como um todo sob carga, simulando múltiplas transações simultâneas para identificar possíveis gargalos ou falhas.

## ⚙️ Configuração do Ambiente

Para executar os testes, siga os passos abaixo:

### 1. Instalação das Dependências

Certifique-se de que o [Node.js](https://nodejs.org/) está instalado em sua máquina. Em seguida, instale as dependências do projeto:

```bash
npm install
