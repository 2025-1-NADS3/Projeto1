const express = require('express');
const cors = require('cors');
require('dotenv').config();
const db = require('./config/db');
const userRoutes = require('./routes/userRoutes');

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());
app.use(cors());

app.use('/api', userRoutes);

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});