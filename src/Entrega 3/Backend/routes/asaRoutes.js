import express from 'express';
import { listarServicosASA, pagarServicos } from '../controllers/asaController.js';
import autenticarToken from '../middlewares/autenticarToken.js';

const router = express.Router();

router.get('/listarServicoAsa', listarServicosASA);
router.post('/pagarServicos', autenticarToken, pagarServicos);

export default router;
