DROP DATABASE senac_sorveteriafx;

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
);

CREATE TABLE saldo(
	quantia DOUBLE NOT NULL
);

INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Chocolate");
INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Flocos");
INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Blue Ice");
INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Morango");
INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Baunilha");
INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, "Cookies and Cream");

INSERT INTO saldo VALUES(0.00)