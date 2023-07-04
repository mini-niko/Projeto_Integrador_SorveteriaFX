CREATE DATABASE senac_sorveteriafx;
USE senac_sorveteriafx;

CREATE TABLE sorvete(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sabor VARCHAR(100) NOT NULL,
    quantidade DOUBLE NOT NULL,
    data_entrada DATE NOT NULL
)