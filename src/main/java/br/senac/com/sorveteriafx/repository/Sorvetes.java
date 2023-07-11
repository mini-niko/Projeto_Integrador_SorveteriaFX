package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.service.SorvetesDBServices;

import java.util.List;

public interface Sorvetes {
    public void salvarSorvete (Sorvete sorvete);
    public void atualizarSorvete (Sorvete sorvete);
    public void apagarSorvete (Sorvete sorvete);
    public Double buscarQuantidadeSorvete (int id);
    public Double buscarSaldo (int id);
    public List<Sorvete> buscarTodosOsSorvetes();
    public Sorvete buscarUmSorvete(int id);
    public static Sorvetes getNewInstance(){
        return new SorvetesDBServices();
    }
}
