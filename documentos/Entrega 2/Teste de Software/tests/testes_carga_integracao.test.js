import request from 'supertest';
import app from '../../../../src/Entrega 2/Backend/server.js';
import db from '../../../../src/Entrega 2/Backend/config/db.js';
import bcrypt from 'bcrypt';

// TESTE DE INTEGRAÇÃO PIX - SIMULADA - TEMPO DE CARGA
console.log("========== TESTE DE INTEGRAÇÃO PIX - SIMULADAS ==========");

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
        ['Teste PIX', '00000000000', 'pix@test.com', '11999999999', senhaCriptografada, 10000, 0]
      );
  
      // Guarda o ID do usuário criado para ser usado nos testes
      usuarioId = resultado.insertId;
});

 // Primeiro teste: criar uma cobrança Pix
 it('Deve gerar uma cobrança Pix com sucesso', async () => {
    const inicio = Date.now();

    // Executar 1000 cobranças simultâneas
    const requisicoes = Array.from({ length: 1000 }).map(() =>
      request(app)
        .post('/pix/gerar-cobranca')
        .send({ usuario_id: usuarioId, valor: valorPix })
    );

    const respostas = await Promise.all(requisicoes);

    // Verifica se todas foram bem-sucedidas
    respostas.forEach(res => {
      expect(res.status).toBe(200);  // Espera-se que a resposta tenha status 200 (sucesso)
      expect(res.body.qr_code).toBeDefined();
      expect(res.body.chave_pix).toBeDefined();
      expect(res.body.transacao_id).toBeDefined();
    });

    // Guarda o ID da primeira transação para ser usado no próximo teste
    transacaoId = respostas[0].body.transacao_id;

    const fim = Date.now();
    console.log(`Tempo - Geração de 1000 cobranças Pix: ${fim - inicio}ms`);
  });

  // Segundo teste: simular o recebimento do pagamento via webhook
  it('Deve confirmar o pagamento via webhook e atualizar saldo', async () => {
    const inicio = Date.now();

    // Envia uma requisição POST para simular o webhook de confirmação
    const resultadoWebhook = await request(app)
      .post('/pix/webhook')
      .send({ transacao_id: transacaoId });

    expect(resultadoWebhook.status).toBe(200); // Espera-se que a resposta tenha status 200

    expect(resultadoWebhook.body.mensagem).toBe("Transação confirmada."); // Verifica se a mensagem de confirmação foi recebida

    const saldoUsuario = await request(app).get(`/pix/saldo/${usuarioId}`); // Consulta o saldo do usuário após o pagamento

    expect(saldoUsuario.status).toBe(200); // Espera-se que a consulta de saldo funcione corretamente

    // Verifica se o saldo foi atualizado corretamente: 10000 (inicial) + 50 (Pix recebido) = 10050 no total
    expect(parseFloat(saldoUsuario.body.saldo)).toBeGreaterThanOrEqual(10050);

    const fim = Date.now();
    console.log(`Tempo - Webhook e saldo atualizado: ${fim - inicio}ms`);
  });


  // Terceiro teste: simular o envio de um Pix para outro usuário
  it('Deve enviar um Pix e aumentar os pontos do usuário', async () => {
    const inicio = Date.now();

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

    // Verifica se o saldo foi reduzido corretamente: 10050 - 25 = 10025
    expect(saldoFinal.status).toBe(200);
    expect(parseFloat(saldoFinal.body.saldo)).toBeGreaterThanOrEqual(10025);

    const fim = Date.now();
    console.log(`Tempo - Envio de Pix e pontos: ${fim - inicio}ms`);
  });
});


// TESTE DE INTEGRAÇÃO PIX COM SENHA ERRADA - SIMULADA - TEMPO DE CARGA
console.log("========== TESTE DE INTEGRAÇÃO PIX COM SENHA ERRADA - SIMULADA ==========");

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
        ['Usuário Teste', '11111111111', 'pix1@test.com', '11988888888', senhaCriptografada, 1000, 0]
      );
  
      // Guarda o ID do usuário criado para ser usado nos testes
      usuarioId = resultado.insertId;
});

  // Teste: Enviar um pix digitando a senha errada
  it('Não deve permitir envio de Pix com senha incorreta', async () => {
    const inicio = Date.now();

    // Realiza 1000 tentativas de envio com senha errada
    const requisicoes = Array.from({ length: 1000 }).map(() =>
      request(app)
        .post('/pix/enviar')
        .send({
          usuario_id: usuarioId,
          valor: 10,
          chave_pix_destino: 'teste@fake.com',
          senha: 'senha1234' // Senha incorreta
        })
    );

    const respostas = await Promise.all(requisicoes);

    // Verifica se todas as respostas foram 401 com a mensagem esperada
    respostas.forEach(res => {
      expect(res.status).toBe(401); // Deve retornar erro 401 (não autorizado)
      expect(res.body.erro).toBe("Senha incorreta.");
    });

    const fim = Date.now();
    console.log(`Tempo - 1000 tentativas com senha incorreta: ${fim - inicio}ms`);
  }, 15000);
});
