const express = require('express');
const { login, register } = require('../controllers/userController');
const { atualizarPerfil } = require('../controllers/userController');
const { deletarPerfil } = require('../controllers/userController');
const autenticarToken = require('../middlewares/autenticarToken');
const router = express.Router();

router.post('/login', login);
router.post('/cadastro', register);
router.put('/atualizar-perfil', autenticarToken, atualizarPerfil);
router.delete('/deletar-perfil', autenticarToken, deletarPerfil);

module.exports = router;