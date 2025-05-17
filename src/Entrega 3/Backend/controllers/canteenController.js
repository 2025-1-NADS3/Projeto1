import db from '../config/db.js';
import bcrypt from 'bcrypt';

// Lista os alimentos da cantina
export const listarCantina = async (req, res) => {
    try {
        const [results] = await db.execute("SELECT * FROM cantina");

        if (results.length === 0) {
            return res.status(404).json({ erro: "Nenhum alimento da cantina encontrado." });
        }

        res.json(results);
    } catch (err) {
        console.error("Erro ao listar itens da cantina:", err);
        res.status(500).json({ erro: "Erro interno no servidor." });
    }
};

// Pagar por alimentos da cantina
export async function pagarCantina(req, res) {
    const { usuario_id, senha, itens_ids } = req.body;

    if (!Array.isArray(itens_ids) || itens_ids.length === 0) {
        return res.status(400).json({ erro: "Nenhum item da cantina selecionado." });
    }

    try {
        const [userRows] = await db.execute("SELECT * FROM usuarios WHERE id = ?", [usuario_id]);
        if (userRows.length === 0) {
            return res.status(400).json({ erro: "Usuário não encontrado." });
        }
        const user = userRows[0];

        const senhaCorreta = await bcrypt.compare(senha, user.senha);
        if (!senhaCorreta) {
            return res.status(401).json({ erro: "Senha incorreta." });
        }

        const placeholders = itens_ids.map(() => '?').join(', ');
        const [itens] = await db.execute(
            `SELECT id, titulo, descricao, preco FROM cantina WHERE id IN (${placeholders})`,
            itens_ids
        );

        if (itens.length === 0) {
            return res.status(400).json({ erro: "Nenhum item válido encontrado." });
        }

        const valor_total = itens.reduce((soma, item) => soma + parseFloat(item.preco), 0);
        if (user.saldo < valor_total) {
            return res.status(400).json({ erro: "Saldo insuficiente." });
        }

        const pontosGanhos = Math.floor(valor_total / 5);
        const dataTransacao = new Date().toISOString().split('T')[0];

        await db.execute(
            "UPDATE usuarios SET saldo = saldo - ?, pontos = pontos + ? WHERE id = ?",
            [valor_total, pontosGanhos, usuario_id]
        );

        await db.execute(
            "INSERT INTO historico_pontos (id_usuario, mes, pontos_usados) VALUES (?, ?, ?)",
            [usuario_id, dataTransacao, pontosGanhos]
        );

        const descricaoItens = itens.map(item => item.titulo).join(', ');
        const chavePix = "cantina@edu.fecap.br";

        // ➕ Geração da senha do pedido
        const [result] = await db.execute(
            "SELECT MAX(senha_pedido) AS ultimaSenha FROM transacoes WHERE senha_pedido IS NOT NULL"
        );
        let novaSenha = 1;
        if (result[0].ultimaSenha !== null) {
            novaSenha = result[0].ultimaSenha + 1;
        }

        await db.execute(
            `INSERT INTO transacoes 
            (usuario_id, tipo, valor, descricao, chave_pix, status, senha_pedido) 
            VALUES (?, 'saida', ?, ?, ?, 'confirmado', ?)`,
            [
                usuario_id,
                valor_total,
                `Pagamento na Cantina: ${descricaoItens}`,
                chavePix,
                novaSenha
            ]
        );

        res.json({
            mensagem: "Pagamento de itens da cantina realizado com sucesso.",
            senha_pedido: novaSenha,
            descricao: descricaoItens,
            subTitulo: itens.map(item => item.descricao).join(', '),
            chave_pix: chavePix,
            valor_total,
            data: dataTransacao,
            pontos_ganhos: pontosGanhos,
            itens: itens.map(item => ({ id: item.id, titulo: item.titulo, preco: item.preco }))
        });

    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}