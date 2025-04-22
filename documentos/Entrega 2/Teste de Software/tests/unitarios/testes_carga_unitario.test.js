import bcrypt from 'bcrypt';

// Funções Unitarias Simuladas
function cadastrar(email, cpf, usuarios) {
  if (!email.endsWith('@edu.fecap.br')) return "Email inválido.";
  if (usuarios.some(u => u.email === email)) return "Email já cadastrado.";
  if (usuarios.some(u => u.cpf === cpf)) return "CPF já cadastrado.";
  usuarios.push({ email, cpf });
  return "Cadastro realizado com sucesso.";
}

function login(cpf, senha, usuarios) {
  const usuario = usuarios.find(u => u.cpf === cpf);
  if (!usuario) return "Usuário não encontrado.";
  const senhaCorreta = bcrypt.compareSync(senha, usuario.senha);
  if (!senhaCorreta) return "Senha incorreta.";
  return "Login bem-sucedido";
}

function trocarPontos(produto, usuario) {
  if (usuario.pontos < produto.pontos_necessarios) {
    return "Você não tem pontos suficientes.";
  }
  usuario.pontos -= produto.pontos_necessarios;
  return "Troca realizada com sucesso!";
}

function consultarPontos(usuario) {
  return { status: 200, pontos: usuario.pontos };
}

// Teste de Carga medindo o tempo

function executarTesteCargaComTempos(quantidade) {
  const senha = 'senha123';
  const senhaErrada = 'senha1234';
  const senhaHash = bcrypt.hashSync(senha, 10);

  console.log("========== TESTES UNITÁRIOS ==========");

  // Cadastro
  const usuarios = [];
  const inicioCadastro = Date.now();
  for (let i = 0; i < quantidade; i++) {
    const email =`usuario${i}@edu.fecap.br`;
    const cpf = `000000000${i.toString().padStart(3, '0')}`;
    cadastrar(email, cpf, usuarios);
  }
  const fimCadastro = Date.now();
  console.log(`Cadastro: ${fimCadastro - inicioCadastro}ms`);

  // Login
  const usuariosLogin = [{ cpf: '12345678900', senha: senhaHash }];
  const inicioLogin = Date.now();
  for (let i = 0; i < quantidade; i++) {
    login('12345678900', senha, usuariosLogin);
    login('12345678900', senhaErrada, usuariosLogin);
    login('00000000000', senha, usuariosLogin);
  }
  const fimLogin = Date.now();
  console.log(`Login (3 variações): ${fimLogin - inicioLogin}ms`);

  // Troca de Pontos
  const produto = { nome: "Produto A", pontos_necessarios: 100 };
  const usuarioTroca = { id: 1, pontos: 200 };
  const inicioTroca = Date.now();
  for (let i = 0; i < quantidade; i++) {
    trocarPontos(produto, usuarioTroca);
  }
  const fimTroca = Date.now();
  console.log(`Troca de Pontos: ${fimTroca - inicioTroca}ms`);

  // Consulta de Pontos
  const usuarioConsulta = { id: 2, pontos: 50 };
  const inicioConsulta = Date.now();
  for (let i = 0; i < quantidade; i++) {
    consultarPontos(usuarioConsulta);
  }
  const fimConsulta = Date.now();
  console.log(`Consulta de Pontos: ${fimConsulta - inicioConsulta}ms`);
}
  
// Executar teste completo
executarTesteCargaComTempos(1000);