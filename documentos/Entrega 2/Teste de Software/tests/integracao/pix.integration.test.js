import request from 'supertest'; // Importação do supertest, fazer requisições HTTP simuladas à nossa API
import app from '../../../../../src/Entrega 2/Backend/server.js'; // Importação do app que representa nosso servidor
import db from '../../../../../src/Entrega 2/Backend/config/db.js'; // Importação da conexão com o banco de dados
import bcrypt from 'bcrypt'; // Importação do bcrypt para criptografar a senha (como é feito no sistema real)

// Início da suíte de testes com Jest
describe('Testes de integração - PIX', () => {
  // Variáveis que vamos usar entre os testes
  let usuarioId;
  let transacaoId;

  // Definindo a senha e o valor que será usado nos testes
  const senha = 'senha123';
  const valorPix = 50;

  // Função que roda antes de todos os testes
  beforeAll(async () => {
    // Criptografa a senha como se fosse no cadastro real
    const senhaCriptografada = await bcrypt.hash(senha, 10);

    // Insere um usuário de teste no banco de dados
    const [resultado] = await db.execute(
      "INSERT INTO usuarios (nome, cpf, email, telefone, senha, saldo, pontos) VALUES (?, ?, ?, ?, ?, ?, ?)",
      ['Teste PIX', '00000000000', 'pix@test.com', '11999999999', senhaCriptografada, 100, 0]
    );

    // Guarda o ID do usuário criado para ser usado nos testes
    usuarioId = resultado.insertId;
  });

  // Primeiro teste: criar uma cobrança Pix
  it('Deve gerar uma cobrança Pix com sucesso', async () => {
    // É enviado uma requisição POST para gerar a cobrança
    const resultadoCobranca = await request(app)
      .post('/pix/gerar-cobranca')
      .send({ usuario_id: usuarioId, valor: valorPix });

    expect(resultadoCobranca.status).toBe(200);  // Espera-se que a resposta tenha status 200 (sucesso)

    // Verifica se o QR Code, a chave Pix e o ID da transação foram retornados
    expect(resultadoCobranca.body.qr_code).toBeDefined();
    expect(resultadoCobranca.body.chave_pix).toBeDefined();
    expect(resultadoCobranca.body.transacao_id).toBeDefined();

    // Guarda o ID da transação para ser usado no próximo teste
    transacaoId = resultadoCobranca.body.transacao_id;
  });

  // Segundo teste: simular o recebimento do pagamento via webhook
  it('Deve confirmar o pagamento via webhook e atualizar saldo', async () => {
    // Envia uma requisição POST para simular o webhook de confirmação
    const resultadoWebhook = await request(app)
      .post('/pix/webhook')
      .send({ transacao_id: transacaoId });

    expect(resultadoWebhook.status).toBe(200); // Espera-se que a resposta tenha status 200

    expect(resultadoWebhook.body.mensagem).toBe("Transação confirmada."); // Verifica se a mensagem de confirmação foi recebida

    const saldoUsuario = await request(app).get(`/pix/saldo/${usuarioId}`); // Consulta o saldo do usuário após o pagamento

    expect(saldoUsuario.status).toBe(200); // Espera-se que a consulta de saldo funcione corretamente

    // Verifica se o saldo foi atualizado corretamente: 100 (inicial) + 50 (Pix recebido) = 150 no total
    expect(parseFloat(saldoUsuario.body.saldo)).toBe(150);
  });

  // Terceiro teste: simular o envio de um Pix para outro usuário
  it('Deve enviar um Pix e aumentar os pontos do usuário', async () => {
    // Envia uma requisição POST para simular o enviar de um Pix (25 reais)
    const resultadoEnvioPix = await request(app)
      .post('/pix/enviar')
      .send({
        usuario_id: usuarioId,
        valor: 25,
        chave_pix_destino: 'teste@fake.com',
        senha 
      });

    expect(resultadoEnvioPix.status).toBe(200); // Espera-se que a resposta tenha status 200

    expect(resultadoEnvioPix.body.mensagem).toBe("Pix enviado.");  // Verifica se a mensagem de confirmação foi retornada

    // Consulta os pontos do usuário e atribui para a variavel pontosUsuario
    const pontosUsuario = await request(app).get(`/pix/pontos/${usuarioId}`);

    // Esperamos status 200 na resposta
    expect(pontosUsuario.status).toBe(200);

    // Verifica se os pontos aumentaram corretamente: 25 reais enviados = 5 pontos (25 / 5)
    expect(parseFloat(pontosUsuario.body.pontos)).toBe(5);

    // Consulta o saldo final do usuário
    const saldoFinal = await request(app).get(`/pix/saldo/${usuarioId}`);

    // Verifica se o saldo foi reduzido corretamente: 150 - 25 = 125
    expect(saldoFinal.status).toBe(200);
    expect(parseFloat(saldoFinal.body.saldo)).toBe(125);
  });
});
