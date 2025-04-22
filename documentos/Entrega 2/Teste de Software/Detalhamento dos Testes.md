
## 🧪 Detalhamento dos Testes

## 📁Estrutura de Pasta
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


### ✨ Testes Unitários

- **`cadastro_unitario_test.js`**: Verifica o processo de cadastro de usuários, assegurando que os dados são armazenados corretamente e que as validações estão funcionando conforme o esperado.

- **`consultaPontos_unitario_test.js`**: Testa a funcionalidade de consulta de pontos, garantindo que o sistema retorna o saldo correto para diferentes cenários.

- **`login_unitario_test.js`**: Avalia o processo de autenticação de usuários, incluindo casos de sucesso e falha (e.g., senha incorreta).

- **`trocaPontos_unitario_test.js`**: Testa a conversão de pontos por recompensas, verificando se a lógica de dedução e validação de pontos está correta.

- **`testes_carga_unitario.test.js`**: Executa testes de carga em funções individuais para avaliar o desempenho sob diferentes volumes de dados.

### ✨ Testes de Integração

- **`pix.integration.test.js`**: Simula transações via PIX, testando a integração entre os módulos de transferência e autenticação.

- **`pix.senha.incorreta.test.js`**: Verifica o comportamento do sistema quando uma transação PIX é tentada com senha incorreta, assegurando que a transação é bloqueada e que mensagens de erro apropriadas são exibidas.

- **`testes_carga_integracao.test.js`**: Avalia o desempenho do sistema como um todo sob carga, simulando múltiplas transações simultâneas para identificar possíveis gargalos ou falhas.

## ⚙️ Configuração do Ambiente

Para executar os testes, siga os passos abaixo:

### 1. Instalação das Dependências

Certifique-se de que o [Node.js](https://nodejs.org/) está instalado em sua máquina. Em seguida, instale as dependências do projeto:

´´´
npm install
´´´
## 2. Execução dos Testes
Para rodar todos os testes:

´´´
npm test
´´´
Para executar um teste específico:
´´´
npx jest nome_do_arquivo_test.js
´´´
## 📄 Configurações Adicionais

jest.config.js: Arquivo de configuração do Jest, onde são definidos parâmetros como o ambiente de teste e mapeamentos de módulos.

package.json: Contém as dependências do projeto e scripts úteis, como o comando para executar os testes.

## 🧰 Tecnologias Utilizadas

Jest: Framework de testes em JavaScript, utilizado para escrever e executar os testes automatizados.

Node.js: Ambiente de execução JavaScript no lado do servidor.

## 📌 Observações
Os testes foram desenvolvidos seguindo as boas práticas de testes automatizados, incluindo a separação entre testes unitários e de integração.

## 😁 Autores
Desenvolvido por:
<a href="https://www.linkedin.com/in/alexandra-christine-silva-590092257">Alexandra Christine </a>,<a href="https://www.linkedin.com/in/gabrielly-cintra/">Gabrielly Cintra de Jesus	</a>, <a href="https://linkedin.com/in/hebert-/">Hebert dos Reis Esteves	</a>, <a href="https://www.linkedin.com/in/jos%C3%A9-almeida-80063a256/">José Bento Almeida Gama </a>
