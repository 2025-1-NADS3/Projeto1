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
        await db.execute("UPDATE transacoes SET status = 'confirmado' WHERE id = ?", [transacao_id]);

        const result = await db.execute("SELECT usuario_id, valor FROM transacoes WHERE id = ?", [transacao_id]);
        const rows = result[0];
        if (rows.length > 0) {
            const { usuario_id, valor } = rows[0];
            await db.execute("UPDATE usuarios SET saldo = saldo + ? WHERE id = ?", [valor, usuario_id]);
        }

        res.json({ mensagem: "Transação confirmada." });
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

        const pontosGanhos = Math.floor(valor / 5);

        await db.execute(
            "UPDATE usuarios SET saldo = saldo - ?, pontos = pontos + ? WHERE id = ?",
            [valor, pontosGanhos, usuario_id]
        );

        await db.execute(
            "INSERT INTO transacoes (usuario_id, tipo, valor, descricao, chave_pix, status) VALUES (?, 'saida', ?, 'Envio Pix', ?, 'confirmado')",
            [usuario_id, valor, chave_pix_destino]
        );
        
        res.json({ mensagem: "Pix enviado." });

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
