import db from '../config/db.js';
import bcrypt from 'bcrypt';

export const listarProduto = async (req, res) => {
    try {
        const [results] = await db.execute("SELECT * FROM produtos");

        if (results.length === 0) {
            return res.status(404).json({ erro: "Nenhum produto encontrado." });
        }

        res.json(results);
    } catch (err) {
        console.error("Erro ao listar produtos:", err);
        res.status(500).json({ erro: "Erro interno no servidor." });
    }
};

export async function pagarProdutoComPontos(req, res) {
    const { usuario_id, senha, itens_ids } = req.body;

    if (!Array.isArray(itens_ids) || itens_ids.length === 0) {
        return res.status(400).json({ erro: "Nenhum produto selecionado." });
    }

    try {
        const [userRows] = await db.execute("SELECT * FROM usuarios WHERE id = ?", [usuario_id]);
        if (userRows.length === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }

        const user = userRows[0];

        const senhaCorreta = await bcrypt.compare(senha, user.senha);
        if (!senhaCorreta) {
            return res.status(401).json({ erro: "Senha incorreta." });
        }

        const placeholders = itens_ids.map(() => '?').join(', ');
        const [produtos] = await db.execute(
            `SELECT id, titulo, descricao, pontos FROM produtos WHERE id IN (${placeholders})`,
            itens_ids
        );

        if (produtos.length === 0) {
            return res.status(400).json({ erro: "Nenhum produto válido encontrado." });
        }

        const pontosTotais = produtos.reduce((total, p) => total + parseInt(p.pontos), 0);

        if (user.pontos < pontosTotais) {
            return res.status(400).json({ erro: "Pontos insuficientes para concluir a troca." });
        }

        await db.execute(
            "UPDATE usuarios SET pontos = pontos - ? WHERE id = ?",
            [pontosTotais, usuario_id]
        );

        const dataTransacao = new Date().toISOString().split('T')[0];

        const [resultadoSenha] = await db.execute(
            "SELECT MAX(senha_pedido) AS ultimaSenha FROM transacoes WHERE senha_pedido IS NOT NULL"
        );
        const novaSenha = resultadoSenha[0].ultimaSenha ? resultadoSenha[0].ultimaSenha + 1 : 1;

        const descricaoItens = produtos.map(p => p.titulo).join(', ');
        const descricaoDetalhada = produtos.map(p => p.descricao).join(', ');

        await db.execute(
            `INSERT INTO transacoes 
            (usuario_id, tipo, valor, descricao, chave_pix, status, senha_pedido) 
            VALUES (?, 'saida', ?, ?, ?, 'confirmado', ?)`,
            [
                usuario_id,
                0, 
                `Troca por Pontos: ${descricaoItens}`,
                `atleticafecap@edu.fecap.br`, 
                novaSenha
            ]
        );

        res.json({
            mensagem: "Troca de produtos realizada com sucesso.",
            senha_pedido: novaSenha,
            descricao: descricaoItens,
            subTitulo: descricaoDetalhada,
            pontos_gastos: pontosTotais,
            data: dataTransacao,
            itens: produtos.map(p => ({
                id: p.id,
                titulo: p.titulo,
                pontos: p.pontos
            }))
        });

    } catch (error) {
        console.error("Erro ao realizar a troca por pontos:", error);
        res.status(500).json({ erro: "Erro interno no servidor." });
    }
}
