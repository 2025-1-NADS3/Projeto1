import express from 'express';
import { gerarCobranca, webhook, enviarPix, consultarSaldo } from '../controllers/pixController.js';

const router = express.Router();

router.post("/gerar-cobranca", gerarCobranca);
router.post("/webhook", webhook);
router.post("/enviar", enviarPix);
router.get("/saldo/:id", consultarSaldo);

export default router;
