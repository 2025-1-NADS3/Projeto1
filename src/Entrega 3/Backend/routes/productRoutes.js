import express from 'express';
import { listarProduto, pagarProdutoComPontos } from '../controllers/productController.js';
import autenticarToken from '../middlewares/autenticarToken.js';

const router = express.Router();

router.get('/listarProduto', listarProduto);
router.post('/pagarProdutoComPontos', autenticarToken, pagarProdutoComPontos);

export default router;
