import express from 'express';
import { listarCantina, pagarCantina } from '../controllers/canteenController.js';
import autenticarToken from '../middlewares/autenticarToken.js';

const router = express.Router();

router.get('/listarCantina', listarCantina);
router.post('/pagarCantina', autenticarToken, pagarCantina);

export default router;
