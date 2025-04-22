// Função de consulta de pontos
function consultarPontos(id, usuarios) {
    const usuario = usuarios.find(u => u.id === id);
    if (!usuario) return "Usuário não encontrado.";
    return `Pontos: ${usuario.pontos}`;
}

// Função para testar rotina de consulta de pontos
function testar_consulta_pontos(id, usuarios, resultadoEsperado) {
    const resultado = consultarPontos(id, usuarios);
    if (resultado === resultadoEsperado) {
        console.log(`Teste passou: consulta de pontos com ID ${id} => ${resultado}`);
    } else {
        console.log(`Teste falhou: consulta de pontos com ID ${id} => ${resultado} (esperado: ${resultadoEsperado})`);
    }
}

const usuariosTeste = [
    { id: 1, pontos: 150 },
    { id: 2, pontos: 0 }
];

// Casos de teste

// 1) Consulta de pontos bem-sucedida
// Usuário com ID 1 possui 150 pontos.
// Resultado esperado: "Pontos: 150"
testar_consulta_pontos(1, usuariosTeste, "Pontos: 150");

// 2) Consulta de pontos com saldo zerado
// Usuário com ID 2 possui 0 pontos.
// Resultado esperado: "Pontos: 0"
testar_consulta_pontos(2, usuariosTeste, "Pontos: 0");

// 3) Usuário não encontrado
// Nenhum usuário com ID 3 existe na lista.
// Resultado esperado: "Usuário não encontrado."
testar_consulta_pontos(3, usuariosTeste, "Usuário não encontrado.");