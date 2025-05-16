import db from '../config/db.js';
import bcrypt from 'bcrypt';

export const listarServicosASA = async (req, res) => {
    try {
        const [results] = await db.execute("SELECT * FROM asa_servicos");

        if (results.length === 0) {
            return res.status(404).json({ erro: "Nenhum serviço encontrado." });
        }

        res.json(results);
    } catch (err) {
        console.error("Erro ao listar serviços:", err);
        res.status(500).json({ erro: "Erro interno no servidor." });
    }
};

export async function pagarServicos(req, res) {
    const { usuario_id, senha, servicos_ids } = req.body;

    if (!Array.isArray(servicos_ids) || servicos_ids.length === 0) {
        return res.status(400).json({ erro: "Nenhum serviço selecionado." });
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

        const placeholders = servicos_ids.map(() => '?').join(', ');
        const [servicos] = await db.execute(
            `SELECT id, titulo, preco FROM asa_servicos WHERE id IN (${placeholders})`,
            servicos_ids
        );

        if (servicos.length === 0) {
            return res.status(400).json({ erro: "Nenhum serviço válido encontrado." });
        }

        const valor_total = servicos.reduce((soma, s) => soma + parseFloat(s.preco), 0);

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

        const descricaoServicos = servicos.map(s => s.titulo).join(', ');
        const chavePix = "asa@edu.fecap.br";

        await db.execute(
            "INSERT INTO transacoes (usuario_id, tipo, valor, descricao, chave_pix, status) VALUES (?, 'saida', ?, ?, ?, 'confirmado')",
            [
                usuario_id,
                valor_total,
                `Pagamento de serviços ASA: ${descricaoServicos}`,
                `${chavePix}`
            ]
        );

        res.json({
            mensagem: "Pagamento de serviços realizado com sucesso.",
            descricao: descricaoServicos,
            chave_pix: chavePix,
            valor_total,
            data: dataTransacao,
            pontos_ganhos: pontosGanhos,
            servicos: servicos.map(s => ({ id: s.id, titulo: s.titulo, preco: s.preco }))
        });

    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}

