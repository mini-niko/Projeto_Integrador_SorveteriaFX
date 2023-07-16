package br.senac.com.sorveteriafx.repository;

import br.senac.com.sorveteriafx.model.SorveteVenda;
import br.senac.com.sorveteriafx.service.SorvetesVendaDBServices;

import java.util.List;

public interface SorvetesVenda {
    public void salvarSorvete (SorveteVenda sorveteVenda);
    public void atualizarSorvete (SorveteVenda sorveteVenda);
    public void apagarSorvete (int id);
    public List<SorveteVenda> buscarTodosOsSorvetes();
    public SorveteVenda buscarUmSorvete(int id);
    public static SorvetesVenda getNewInstance(){
        return new SorvetesVendaDBServices();
    }
}
