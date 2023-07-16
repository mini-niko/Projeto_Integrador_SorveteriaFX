CREATE DATABASE senac_sorveteriafx;
USE senac_sorveteriafx;

CREATE TABLE sorvete(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quantidade DOUBLE NOT NULL,
    sabor VARCHAR(100) NOT NULL
);

CREATE TABLE sorvete_mov(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_sabor BIGINT NOT NULL,
    quantidade DOUBLE NOT NULL,
    tipo_mov DOUBLE NOT NULL,
    data_mov DATE NOT NULL,
    preco DOUBLE NOT NULL,
    
    FOREIGN KEY (id_sabor) REFERENCES sorvete(id)
)