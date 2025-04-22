import request from 'supertest'; // Importação do supertest, fazer requisições HTTP simuladas à nossa API
import app from '../../../../src/Entrega 2/Backend/server.js'; // Importação do app que representa nosso servidor
import db from '../../../../src/Entrega 2/Backend/config/db.js'; // Importação da conexão com o banco de dados
import bcrypt from 'bcrypt'; // Importação do bcrypt para criptografar a senha (como é feito no sistema real)

// Início da suíte de testes com Jest
describe('Teste de integração - Pix com senha incorreta', () => {
  // Variáveis que vamos usar entre os testes
  let usuarioId;
  const senhaCorreta = 'senha123';

  // Função que roda antes de todos os testes
  beforeAll(async () => {
    // Criptografa a senha como se fosse no cadastro real
    const senhaCriptografada = await bcrypt.hash(senhaCorreta, 10);

    // Insere um usuário de teste no banco de dados
    const [resultado] = await db.execute(
      "INSERT INTO usuarios (nome, cpf, email, telefone, senha, saldo, pontos) VALUES (?, ?, ?, ?, ?, ?, ?)",
      ['Usuário Teste', '11111111111', 'pix1@test.com', '11988888888', senhaCriptografada, 100, 0]
    );

    // Guarda o ID do usuário criado para ser usado nos testes
    usuarioId = resultado.insertId;
  });

  // Teste: Enviar um pix digitando a senha errada
  it('Não deve permitir envio de Pix com senha incorreta', async () => {
     // É enviado uma requisição POST para realizar o pix
    const resposta = await request(app)
      .post('/pix/enviar')
      .send({
        usuario_id: usuarioId,
        valor: 10,
        chave_pix_destino: 'teste@fake.com',
        senha: 'senha1234' // Enviando senha errada
      });

    expect(resposta.status).toBe(401); // Deve retornar erro 401 (não autorizado)
    expect(resposta.body.erro).toBe("Senha incorreta."); // Mensagem de erro esperada
    console.log(resposta.body); // Imprimir a mensagem de erro
    console.log(resposta.status); // Imprimir o status
  });
});
