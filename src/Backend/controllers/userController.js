const db = require('../config/db');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const JWT_SECRET = 'neonpay_academy';

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
    const { nome, cpf, email, telefone, senha } = req.body;
    if (!nome || !cpf || !email || !telefone || !senha) return res.status(400).json("Por favor, preencha todos os campos.");

    db.query("SELECT * FROM usuarios WHERE email = ? OR cpf = ?", [email, cpf], (erro, usuariosExistentes) => {
        if (usuariosExistentes.length > 0) return res.status(400).json("Email ou CPF já cadastrados.");
        bcrypt.hash(senha, 10, (erro, hash) => {
            if (erro) return res.status(500).json("Erro ao hashear a senha.");
            db.query("INSERT INTO usuarios (nome, cpf, email, telefone, senha) VALUES (?, ?, ?, ?, ?)", [nome, cpf, email, telefone, hash], (erro) => {
                if (erro) return res.status(500).json("Erro ao registrar o usuário.");
                return res.status(201).json("Usuário registrado com sucesso.");
            });
        });
    });
};