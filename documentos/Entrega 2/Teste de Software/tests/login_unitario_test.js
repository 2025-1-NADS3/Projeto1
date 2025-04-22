import bcrypt from 'bcrypt';

// Função de login
function login(cpf, senha, usuarios) {
    const usuario = usuarios.find(u => u.cpf === cpf);
    if (!usuario) return "Usuário não encontrado.";
    
    const senhaCorreta = bcrypt.compareSync(senha, usuario.senha);
    if (!senhaCorreta) return "Senha incorreta.";
    
    return "Login bem-sucedido";
}

// Função para testar rotina de login
function testar_login(cpf, senha, usuarios, resultadoEsperado) {
    const resultado = login(cpf, senha, usuarios);
    
    if (resultado === resultadoEsperado) {
        console.log(`Teste passou: Login com ${cpf} => ${resultado}`);
    } else {
        console.log(`Teste falhou: Login com ${cpf} => ${resultado} (esperado: ${resultadoEsperado})`);
    }
}

const senhaHash = bcrypt.hashSync('senha123', 10); // Salvando a senha já criptografada
const usuarioTeste = [{ cpf: '12345678900', senha: senhaHash }]; // Usuario Teste

// Casos de teste de login

// 1) Login bem-sucedido
// Usuário informa CPF e senha corretos. 
// Resultado esperado: "Login bem-sucedido"
testar_login('12345678900', 'senha123', usuarioTeste, 'Login bem-sucedido'); 

// 2) Senha incorreta
// Usuário informa CPF correto, mas senha incorreta.
// Resultado esperado: "Senha incorreta."
testar_login('12345678900', 'senha1234', usuarioTeste, 'Senha incorreta.'); 

// 3) Usuário não encontrado (CPF inválido)
// Usuário informa CPF inexistente no sistema, mesmo com senha válida.
// Resultado esperado: "Usuário não encontrado."
testar_login('99999999999', 'senha123', usuarioTeste, 'Usuário não encontrado.'); 

// 4) Usuário não encontrado (CPF e senha inválidos)
// Usuário informa CPF e senha que não existem no sistema.
// Resultado esperado: "Usuário não encontrado."
testar_login('99999999999', 'senha1234', usuarioTeste, 'Usuário não encontrado.');
