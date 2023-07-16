package br.senac.com.sorveteriafx.model;

import java.util.Date;

public class SorveteVenda {

    private int id;
    private int sabor;
    private Double quantidade;
    private int tipoMov;
    private Date dataMov;
    private Double preco;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSabor() {
        return sabor;
    }

    public void setSabor(int sabor) {
        this.sabor = sabor;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public int getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(int tipoMov) {
        this.tipoMov = tipoMov;
    }

    public Date getDataMov() {
        return dataMov;
    }

    public void setDataMov(Date dataMov) {
        this.dataMov = dataMov;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
