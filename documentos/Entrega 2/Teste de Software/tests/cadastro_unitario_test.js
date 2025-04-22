// Função de cadastro
function cadastrar(email, cpf, usuarios) {
    if (!email.endsWith('@edu.fecap.br')) return "Email inválido.";
    if (usuarios.some(u => u.email === email)) return "Email já cadastrado.";
    if (usuarios.some(u => u.cpf === cpf)) return "CPF já cadastrado.";
    return "Cadastro realizado com sucesso.";
}

// Função para testar rotina de cadastro
function testar_cadastro(email, cpf, usuarios, resultadoEsperado) {
    const resultado = cadastrar(email, cpf, usuarios);

    if (resultado === resultadoEsperado) {
        console.log(`Teste passou: Cadastro com '${email}', CPF: '${cpf}' => ${resultado}`);
    } else {
        console.log(`Teste falhou: Cadastro com '${email}', CPF: '${cpf}' => ${resultado} (esperado: ${resultadoEsperado})`);
    }
}

// Usuários já cadastrados
const usuariosCadastrados = [
    { email: 'joao@edu.fecap.br', cpf: '12345678900' },
    { email: 'maria@edu.fecap.br', cpf: '12345678901' }
];

// Casos de teste

// 1) E-mail inválido
// Usuário tenta se cadastrar com um e-mail que não pertence ao domínio institucional da Fecap (@edu.fecap.br)
// Resultado esperado: "Email inválido."
testar_cadastro('ana@edu.com', '98765432100', usuariosCadastrados, 'Email inválido.'); 

// 2) E-mail já cadastrado
// Usuário tenta se cadastrar com um novo CPF, mas com um e-mail já existente no sistema.
// Resultado esperado: "Email já cadastrado."
testar_cadastro('joao@edu.fecap.br', '11111111111', usuariosCadastrados, 'Email já cadastrado.'); 

// 3) CPF já cadastrado
// Usuário tenta se cadastrar com um novo e-mail, mas com um CPF já cadastrado.
// Resultado esperado: "CPF já cadastrado."
testar_cadastro('nova@edu.fecap.br', '12345678901', usuariosCadastrados, 'CPF já cadastrado.'); 

// 4) Cadastro realizado com sucesso
// Usuário realiza o cadastro com um e-mail institucional e CPF que ainda não estão cadastrados.
// Resultado esperado: "Cadastro realizado com sucesso."
testar_cadastro('paulo@edu.fecap.br', '22222222222', usuariosCadastrados, 'Cadastro realizado com sucesso.');
