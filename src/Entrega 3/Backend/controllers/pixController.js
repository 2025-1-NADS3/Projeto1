import db from '../config/db.js';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';
import dotenv from 'dotenv';

dotenv.config();
const JWT_SECRET = process.env.JWT_SECRET;

export async function gerarCobranca(req, res) {
    const { usuario_id, valor } = req.body;
    try {
        const fakeQrCode = "https://fake.qr/pix";
        const fakeChave = "pix@neonpay.com";
        
        const [result] = await db.execute(
            "INSERT INTO transacoes (usuario_id, tipo, valor, descricao, chave_pix, status) VALUES (?, 'entrada', ?, 'Depósito via Pix', ?, 'pendente')",
            [usuario_id, valor, fakeChave]
        );
        
        const transacao_id = result.insertId;
        
        res.json({ qr_code: fakeQrCode, chave_pix: fakeChave, transacao_id });
    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}

export async function webhook(req, res) {
    const { transacao_id } = req.body;

    try {
        const [transacaoResultado] = await db.execute("SELECT usuario_id, valor, status FROM transacoes WHERE id = ?", [transacao_id]);

        if (transacaoResultado.length === 0) {
            return res.status(404).json({ erro: "Transação não encontrada." });
        }

        const { usuario_id, valor, status } = transacaoResultado[0];

        if (status === 'confirmado') {
            return res.status(400).json({ erro: "Transação já confirmada." });
        }

        await db.execute("UPDATE transacoes SET status = 'confirmado' WHERE id = ?", [transacao_id]);

        await db.execute("UPDATE usuarios SET saldo = saldo + ? WHERE id = ?", [valor, usuario_id]);

        res.json({ mensagem: "Transação confirmada com sucesso." });

    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}


export async function enviarPix(req, res) {
    const { usuario_id, valor, chave_pix_destino, senha } = req.body;

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

        if (user.saldo < valor) {
            return res.status(400).json({ erro: "Saldo insuficiente." });
        }

        const [destRows] = await db.execute("SELECT * FROM usuarios WHERE chave_pix = ?", [chave_pix_destino]);
        if (destRows.length === 0) {
            return res.status(404).json({ erro: "Destinatário não encontrado." });
        }
        const destinatario = destRows[0];

        const pontosGanhos = Math.floor(valor / 5);
        const dataTransacao = new Date().toISOString().split('T')[0];

        await db.execute(
            "UPDATE usuarios SET saldo = saldo - ?, pontos = pontos + ? WHERE id = ?",
            [valor, pontosGanhos, usuario_id]
        );

        await db.execute(
            "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?",
            [valor, destinatario.id]
        );

        await db.execute(
            "INSERT INTO historico_pontos (id_usuario, mes, pontos_usados) VALUES (?, ?, ?)",
            [usuario_id, dataTransacao, pontosGanhos]
        );

        await db.execute(
            "INSERT INTO transacoes (usuario_id, tipo, valor, descricao, chave_pix, status) VALUES (?, 'saida', ?, 'Envio Pix', ?, 'confirmado')",
            [usuario_id, valor, chave_pix_destino]
        );

        await db.execute(
            "INSERT INTO transacoes (usuario_id, tipo, valor, descricao, chave_pix, status) VALUES (?, 'entrada', ?, 'Recebimento Pix', ?, 'confirmado')",
            [destinatario.id, valor, user.chave_pix || "Sem chave"]
        );

        res.json({ mensagem: "Pix enviado com sucesso.", data: dataTransacao });

    } catch (error) {
        res.status(500).json({ erro: error.message }); 
    }
}


export async function consultarSaldo(req, res) {
    const { id } = req.params;
    try {
        const [rows] = await db.execute("SELECT saldo FROM usuarios WHERE id = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }
        res.json({ saldo: rows[0].saldo });
    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}

export async function consultarPontos(req, res) {
    const { id } = req.params;
    try {
        const [rows] = await db.execute("SELECT pontos FROM usuarios WHERE id = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }
        res.json({ pontos: rows[0].pontos });
    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
}

export async function extrato(req, res) {
    const { usuario_id } = req.params;
  
    try {
      const [transacoes] = await db.execute(
        "SELECT tipo, valor, descricao, chave_pix, status, data FROM transacoes WHERE usuario_id = ? ORDER BY data DESC",
        [usuario_id]
      );
  
      res.json({ usuario_id, extrato: transacoes });
    } catch (error) {
      res.status(500).json({ erro: error.message });
    }
};