CREATE DATABASE senac_sorveteriafx;
USE senac_sorveteriafx;

CREATE TABLE sorvete(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sabor VARCHAR(100) NOT NULL
);

CREATE TABLE venda_sorvete(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_sabor BIGINT NOT NULL,
    quantidade DOUBLE NOT NULL,
    data_venda DATE NOT NULL,
    preco DOUBLE NOT NULL,
    
    FOREIGN KEY (id_sabor) REFERENCES sorvete(id)
)