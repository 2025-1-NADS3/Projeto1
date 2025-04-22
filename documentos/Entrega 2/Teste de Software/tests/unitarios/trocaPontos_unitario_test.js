// Função de troca de pontos
function trocarPontos(produto, usuario) {
    if (usuario.pontos < produto.pontos_necessarios) {
        return "Você não tem pontos suficientes.";
    }

    usuario.pontos -= produto.pontos_necessarios;
    return "Troca realizada com sucesso!";
}

// Função para testar rotina de troca de pontos
function testar_troca_unitaria(produto, usuario, resultadoEsperado) {
    const usuarioCopia = {...usuario}; // Variavel usuarioCopia armezena uma cópia para não alterar o original
    const resultado = trocarPontos(produto, { ...usuarioCopia }); 

    if (resultado === resultadoEsperado) {
        console.log(`Teste passou: Troca => ${resultado}`);
    } else {
        console.log(`Teste falhou: Troca => ${resultado} (esperado: ${resultadoEsperado})`);
    }
}

// Casos de teste

const produtoTeste = { id: 1, nome: 'Produto A', pontos_necessarios: 100 };
const usuarioTeste1 = { id: 1, pontos: 150 }; 
const usuarioTeste2 = { id: 2, pontos: 50 };

// 1) Troca realizada com sucesso
// Usuário possui pontos suficientes para trocar pelo produto.
// Resultado esperado: "Troca realizada com sucesso!"
testar_troca_unitaria(produtoTeste, usuarioTeste1, 'Troca realizada com sucesso!');

// 2) Pontos insuficientes
// Usuário não possui pontos suficientes para a troca.
// Resultado esperado: "Você não tem pontos suficientes."
testar_troca_unitaria(produtoTeste, usuarioTeste2, 'Você não tem pontos suficientes.');