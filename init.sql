-- Cria o banco de dados
CREATE DATABASE paymentsdb;

-- Conecta no banco criado
\c paymentsdb;

-- Cria a tabela
CREATE TABLE payments (
    payment_id VARCHAR(255) PRIMARY KEY,
    buyer_id VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    card VARCHAR(255) NOT NULL
);