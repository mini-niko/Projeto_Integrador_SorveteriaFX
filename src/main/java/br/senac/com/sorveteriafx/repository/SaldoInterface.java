package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.service.SaldoDBService;

public interface SaldoInterface {
    public double buscarSaldo();
    public void atualizarSaldo(double valorParaAtualizar);

    public static SaldoInterface getNewInstance() {
        return new SaldoDBService();
    }
}
