package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.service.SorvetesDBServices;
import br.senac.com.sorveteriafx.service.SorvetesVendaDBServices;

import java.util.List;

public interface Sorvetes {

    public void alterarEstoque (int id, Double quantidade);
    public void adicionarSabor (String sabor);
    public void removerSabor(int id);
    public List<Sorvete> buscarTodosOsSabores();
    public Sorvete buscarUmSabor(int id);

    public static Sorvetes getNewInstance(){
        return new SorvetesDBServices();
    }
}
