package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.service.SorvetesDBServices;
import br.senac.com.sorveteriafx.service.SorvetesVendaDBServices;

import java.util.ArrayList;
import java.util.List;

public interface Sorvetes {

    public void alterarQuantidadeSorvete (int id, Double quantidade);
    public void adicionarSorvete (String sabor);
    public void removerSabor(int id);
    public ArrayList<Sorvete> buscarTodosOsSorvetes();
    public ArrayList<String> buscarTodosOsSabores();
    public Sorvete buscarUmSorvete (int id);
    public String buscarUmSabor (int id);

    public static Sorvetes getNewInstance(){
        return new SorvetesDBServices();
    }
}
