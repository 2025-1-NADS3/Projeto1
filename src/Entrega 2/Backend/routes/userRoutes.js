import express from 'express';
import { login, register, atualizarPerfil, deletarPerfil, getPerfil, trocarPontosPorProduto } from '../controllers/userController.js';
import autenticarToken from '../middlewares/autenticarToken.js';

const router = express.Router();

router.post('/login', login);
router.post('/cadastro', register);
router.put('/atualizar-perfil', autenticarToken, atualizarPerfil);
router.delete('/deletar-perfil', autenticarToken, deletarPerfil);
router.get('/perfil', autenticarToken, getPerfil);
router.post('/trocar-pontos', autenticarToken, trocarPontosPorProduto);

export default router;
