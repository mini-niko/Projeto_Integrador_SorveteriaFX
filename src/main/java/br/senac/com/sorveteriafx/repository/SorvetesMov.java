package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.model.SorveteMov;
import br.senac.com.sorveteriafx.service.SorvetesMovDBServices;

import java.util.List;

public interface SorvetesMov {
    public void salvarSorveteMov (SorveteMov sorveteMov);
    public void atualizarSorveteMov (SorveteMov sorveteMov);
    public void apagarSorveteMov (int id);
    public List<SorveteMov> buscarTodosOsSorvetesMov();
    public SorveteMov buscarUmSorveteMov(int id);
    public static SorvetesMov getNewInstance(){
        return new SorvetesMovDBServices();
    }
}
