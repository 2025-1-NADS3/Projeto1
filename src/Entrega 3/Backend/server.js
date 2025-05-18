import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import db from './config/db.js';
import userRoutes from './routes/userRoutes.js';
import pixRoutes from './routes/pixRoutes.js';
import asaRoutes from './routes/asaRoutes.js';
import canteenRoutes from './routes/canteenRoutes.js';
import productRoutes from './routes/productRoutes.js';

dotenv.config();

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());
app.use(cors());

app.use('/api', userRoutes);
app.use('/pix', pixRoutes);
app.use('/asa', asaRoutes);
app.use('/cantina', canteenRoutes);
app.use('/produto', productRoutes);

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});
