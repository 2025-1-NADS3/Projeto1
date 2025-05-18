import express from 'express';
import { login, register, atualizarPerfil, deletarPerfil, getPerfil, trocarPontosPorProduto, listarHistoricoPontos, buscarUsuarioPorChavePix, cadastrarChavePix, buscarChavePix, excluirChavePix } from '../controllers/userController.js';
import autenticarToken from '../middlewares/autenticarToken.js';

const router = express.Router();

router.post('/login', login);
router.post('/cadastro', register);
router.put('/atualizar-perfil', autenticarToken, atualizarPerfil);
router.delete('/deletar-perfil', autenticarToken, deletarPerfil);
router.get('/perfil', autenticarToken, getPerfil);
router.post('/trocar-pontos', autenticarToken, trocarPontosPorProduto);
router.get('/historico-pontos/:id', listarHistoricoPontos);
router.get('/usuarios/buscar-por-chave-pix/:chave_pix', autenticarToken, buscarUsuarioPorChavePix);
router.post('/cadastrarChavePix', autenticarToken, cadastrarChavePix);
router.get('/buscarChavePix/:usuario_id', autenticarToken, buscarChavePix);
router.post('/excluirChavePix', autenticarToken, excluirChavePix);


export default router;
