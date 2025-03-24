const db = require('../config/db');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
require('dotenv').config();
const JWT_SECRET = process.env.JWT_SECRET; 


exports.login = (req, res) => {
    const { cpf, senha } = req.body;
    if (!cpf || !senha) return res.status(400).json("Por favor, forneça cpf e senha.");

    db.query("SELECT * FROM usuarios WHERE cpf = ?", [cpf], (err, result) => {
        if (err) return res.status(500).json("Erro ao consultar o banco de dados.");
        if (result.length === 0) return res.status(401).json("Usuário não encontrado.");

        const user = result[0];
        bcrypt.compare(senha, user.senha, (err, senhaCorreta) => {
            if (err) return res.status(500).json("Erro ao verificar a senha.");
            if (!senhaCorreta) return res.status(401).json("Senha incorreta.");

            const token = jwt.sign(
                { id: user.id, cpf: user.cpf, nome: user.nome, permissao: 'usuario' },
                JWT_SECRET,
                { expiresIn: '2h' }
            );
            return res.status(200).json({ message: "Login bem-sucedido", token, permissao: 'usuario' });
        });
    });
};

exports.register = (req, res) => {
    const { nome, cpf, data_nasc, email, telefone, senha } = req.body;

    if (!nome || !cpf || !data_nasc || !email || !telefone || !senha) {
        return res.status(400).json({ erro: "Por favor, preencha todos os campos." });
    }

    db.query("SELECT * FROM usuarios WHERE email = ? OR cpf = ?", [email, cpf], (erro, usuariosExistentes) => {
        if (erro) return res.status(500).json({ erro: "Erro no banco de dados." });

        if (usuariosExistentes.length > 0) {
            return res.status(400).json({ erro: "Email ou CPF já cadastrados." });
        }

        bcrypt.hash(senha, 10, (erro, hash) => {
            if (erro) return res.status(500).json({ erro: "Erro ao hashear a senha." });

            db.query("INSERT INTO usuarios (nome, cpf, data_nasc, email, telefone, senha) VALUES (?, ?, ?, ?, ?, ?)", 
                [nome, cpf, data_nasc, email, telefone, hash], 
                (erro) => {
                    if (erro) return res.status(500).json({ erro: "Erro ao registrar o usuário." });

                    return res.json({ mensagem: "Usuário registrado com sucesso." });
                }
            );
        });
    });
};

exports.atualizarPerfil = (req, res) => {
    const { id } = req.user; 
    const { nome, email, telefone, senha } = req.body;

    if (!nome || !email || !telefone) {
        return res.status(400).json({ erro: "Por favor, preencha todos os campos." });
    }

    let queryValues = [nome, email, telefone, id];
    let sqlAtualizar = "UPDATE usuarios SET nome = ?, email = ?, telefone = ? WHERE id = ?";

    if (senha) {
        bcrypt.hash(senha, 10, (err, hashedPassword) => {
            if (err) return res.status(500).json({ erro: "Erro ao hashear a senha." });

            sqlAtualizar = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, senha = ? WHERE id = ?";
            queryValues = [nome, email, telefone, hashedPassword, id];

            db.query(sqlAtualizar, queryValues, (err, resultado) => {
                if (err) return res.status(500).json({ erro: "Erro ao atualizar o perfil." });

                return res.status(200).json({ mensagem: "Perfil atualizado com sucesso." });
            });
        });
    } else {
        db.query(sqlAtualizar, queryValues, (err, resultado) => {
            if (err) return res.status(500).json({ erro: "Erro ao atualizar o perfil." });

            return res.status(200).json({ mensagem: "Perfil atualizado com sucesso." });
        });
    }
};

exports.deletarPerfil = (req, res) => {
    const { id } = req.user; 

    if (!id) {
        return res.status(400).json({ erro: "ID do usuário não fornecido." });
    }

    const sqlDeletar = "DELETE FROM usuarios WHERE id = ?";

    db.query(sqlDeletar, [id], (err, resultado) => {
        if (err) {
            console.error("Erro ao deletar o usuário:", err);
            return res.status(500).json({ erro: "Erro ao deletar o usuário." });
        }

        if (resultado.affectedRows === 0) {
            return res.status(404).json({ erro: "Usuário não encontrado." });
        }

        return res.status(200).json({ mensagem: "Usuário deletado com sucesso." });
    });
};