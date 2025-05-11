import db from '../config/db.js';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';
import dotenv from 'dotenv';

dotenv.config();

const JWT_SECRET = process.env.JWT_SECRET;

export const login = async (req, res) => {
    const { cpf, senha } = req.body;
    if (!cpf || !senha) return res.status(400).json("Por favor, forneça cpf e senha.");

    try {
        const [result] = await db.execute("SELECT * FROM usuarios WHERE cpf = ?", [cpf]);
        if (result.length === 0) return res.status(401).json("Usuário não encontrado.");

        const user = result[0];
        const senhaCorreta = await bcrypt.compare(senha, user.senha);
        if (!senhaCorreta) return res.status(401).json("Senha incorreta.");

        const token = jwt.sign(
            { id: user.id, cpf: user.cpf, nome: user.nome, permissao: 'usuario' },
            JWT_SECRET,
            { expiresIn: '2h' }
        );

        return res.status(200).json({ id: user.id, message: "Login bem-sucedido", token, permissao: 'usuario' });
    } catch (err) {
        return res.status(500).json("Erro ao consultar o banco de dados.");
    }
};

export const register = async (req, res) => {
    const { nome, cpf, data_nasc, email, telefone, senha } = req.body;

    if (!nome || !cpf || !data_nasc || !email || !telefone || !senha) {
        return res.status(400).json({ erro: "Por favor, preencha todos os campos." });
    }

    try {
        const [usuariosExistentes] = await db.execute(
            "SELECT * FROM usuarios WHERE email = ? OR cpf = ?", [email, cpf]
        );

        if (usuariosExistentes.length > 0) {
            return res.status(400).json({ erro: "Email ou CPF já cadastrados." });
        }

        const hash = await bcrypt.hash(senha, 10);

        await db.execute(
            "INSERT INTO usuarios (nome, cpf, data_nasc, email, telefone, senha) VALUES (?, ?, ?, ?, ?, ?)",
            [nome, cpf, data_nasc, email, telefone, hash]
        );

        return res.json({ mensagem: "Usuário registrado com sucesso." });
    } catch (erro) {
        return res.status(500).json({ erro: "Erro ao registrar o usuário." });
    }
};

export const atualizarPerfil = async (req, res) => {
    const { id } = req.user;
    const { nome, email, telefone, senha } = req.body;

    if (!nome || !email || !telefone) {
        return res.status(400).json({ erro: "Por favor, preencha todos os campos." });
    }

    try {
        let queryValues = [nome, email, telefone, id];
        let sqlAtualizar = "UPDATE usuarios SET nome = ?, email = ?, telefone = ? WHERE id = ?";

        if (senha) {
            const hashedPassword = await bcrypt.hash(senha, 10);
            sqlAtualizar = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, senha = ? WHERE id = ?";
            queryValues = [nome, email, telefone, hashedPassword, id];
        }

        await db.execute(sqlAtualizar, queryValues);

        return res.status(200).json({ mensagem: "Perfil atualizado com sucesso." });
    } catch (err) {
        return res.status(500).json({ erro: "Erro ao atualizar o perfil." });
    }
};

export const deletarPerfil = async (req, res) => {
    const { id } = req.user;

    if (!id) {
        return res.status(400).json({ erro: "ID do usuário não fornecido." });
    }

    try {
        const [result] = await db.execute("DELETE FROM usuarios WHERE id = ?", [id]);

        if (result.affectedRows === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }

        return res.status(200).json({ mensagem: "Usuário deletado com sucesso." });
    } catch (err) {
        return res.status(500).json({ erro: "Erro ao deletar o usuário." });
    }
};

export const getPerfil = async (req, res) => {
    const { id } = req.user;

    if (!id) {
        return res.status(400).json({ erro: "ID do usuário não fornecido." });
    }

    try {
        const [result] = await db.execute(
            "SELECT id, nome, email, telefone FROM usuarios WHERE id = ?", [id]
        );

        if (result.length === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }

        return res.status(200).json(result[0]);
    } catch (err) {
        return res.status(500).json({ erro: "Erro ao buscar o perfil." });
    }
};

export const getSaldo = async (req, res) => {
    const { id } = req.params;
    try {
        const [rows] = await db.execute("SELECT saldo FROM usuarios WHERE id = ?", [id]);
        if (rows.length > 0) {
            res.json({ saldo: rows[0].saldo });
        } else {
            res.status(404).json({ erro: "Usuário não encontrado" });
        }
    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
};

export const trocarPontosPorProduto = async (req, res) => {
    const { id } = req.user; 
    const { produtoId } = req.body;

    if (!produtoId) {
        return res.status(400).json({ erro: "ID do produto é obrigatório." });
    }

    try {
        const [produtoRows] = await db.execute("SELECT * FROM produtos WHERE id = ?", [produtoId]);
        if (produtoRows.length === 0) {
            return res.status(404).json({ erro: "Produto não encontrado." });
        }

        const produto = produtoRows[0];

        if (produto.quantidade_estoque <= 0) {
            return res.status(400).json({ erro: "Produto fora de estoque." });
        }

        const [usuarioRows] = await db.execute("SELECT pontos FROM usuarios WHERE id = ?", [id]);
        const pontosUsuario = usuarioRows[0].pontos;

        if (pontosUsuario < produto.pontos_necessarios) {
            return res.status(400).json({ erro: "Você não tem pontos suficientes." });
        }

        const novosPontos = pontosUsuario - produto.pontos_necessarios;
        await db.execute("UPDATE usuarios SET pontos = ? WHERE id = ?", [novosPontos, id]);

        await db.execute("UPDATE produtos SET quantidade_estoque = quantidade_estoque - 1 WHERE id = ?", [produtoId]);

        await db.execute("INSERT INTO historico_trocas (usuario_id, produto_id, data) VALUES (?, ?, NOW())", [id, produtoId]);

        return res.status(200).json({
            mensagem: "Troca realizada com sucesso!",
            produto: produto.nome,
        });

    } catch (error) {
        console.error(error);
        return res.status(500).json({ erro: "Erro ao processar a troca." });
    }
};

export const listarHistoricoPontos = async (req, res) => {
    const { id } = req.params;
    try {
        const [rows] = await db.execute(`
            SELECT 
                DATE_FORMAT(mes, '%Y-%m') AS mes, 
                SUM(pontos_usados) AS total_pontos
            FROM historico_pontos
            WHERE id_usuario = ?
            GROUP BY mes 
            ORDER BY mes
        `, [id]);

        res.json(rows);
    } catch (error) {
        res.status(500).json({ erro: error.message });
    }
};
