import express from 'express';
import { gerarCobranca, webhook, enviarPix, consultarSaldo, consultarPontos, extrato } from '../controllers/pixController.js';

const router = express.Router();

router.post("/gerar-cobranca", gerarCobranca);
router.post("/webhook", webhook);
router.post("/enviar", enviarPix);
router.get("/saldo/:id", consultarSaldo);
router.get("/pontos/:id", consultarPontos);
router.get("/extrato/:usuario_id", extrato);

export default router;
