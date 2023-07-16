package br.senac.com.sorveteriafx.service;

import br.senac.com.sorveteriafx.model.SorveteVenda;
import br.senac.com.sorveteriafx.repository.SorvetesVenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SorvetesVendaDBServices implements SorvetesVenda {
    final String USUARIO = "root";
    final String SENHA = "root";
    final String URL_BANCO = "jdbc:mysql://localhost:3306/senac_sorveteriafx";
    final String CLASS_DRIVER = "com.mysql.jdbc.Driver";
    final String INSERIR = "INSERT INTO sorvete_mov(id_sabor, quantidade, tipo_mov, data_mov, preco) VALUES(?, ?, ?, ?, ?)";
    final String BUSCAR_TODOS = "SELECT id_sabor, quantidade, tipo_mov DATE_FORMAT(data_mov, '%d/%m/%y'), preco FROM sorvete_mov";
    final String ATUALIZAR = "UPDATE sorvete_mov SET id_sabor = ?, quantidade = ?, tipo_mov = ?, data_mov = ?";
    final String APAGAR = "DELETE FROM sorvete_mov WHERE id = ?";
    final String FORMATO_DATA = "dd/MM/yyyy";
    final String FORMATO_DATA_SQL = "yyyy-MM-dd";
    final SimpleDateFormat FORMATADOR = new SimpleDateFormat(FORMATO_DATA);
    final SimpleDateFormat FORMATADOR_SQL = new SimpleDateFormat(FORMATO_DATA_SQL);

    private Connection conexao() {
        try {
            Class.forName(CLASS_DRIVER);
            return DriverManager.getConnection(URL_BANCO, USUARIO, SENHA);
        }
        catch (Exception e) {
            e.printStackTrace();
            if(e instanceof ClassNotFoundException) {
                System.out.println("Verifique se o Driver do Banco de Dados está no ClassPath do projeto");
            }
            else {
                System.out.println("Verifique se o Banco de Dados está rodando e se os dados de conexão estão corretos");
            }
            System.exit(0);
            return null;
        }
    }

    @Override
    public void salvarSorvete(SorveteVenda sorveteVenda) {
        try{
            Connection con = conexao();

            PreparedStatement salvar = con.prepareStatement(INSERIR);
            String dateStr = FORMATADOR_SQL.format(sorveteVenda.getDataMov());
            salvar.setInt(1, sorveteVenda.getSabor());
            salvar.setDouble(2, sorveteVenda.getQuantidade());
            salvar.setInt(3, sorveteVenda.getTipoMov());
            salvar.setString(4, dateStr);
            salvar.setDouble(5, sorveteVenda.getPreco());
            salvar.executeUpdate();
            salvar.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar a conta");
            System.exit(0);
        }
    }

    @Override
    public void atualizarSorvete(SorveteVenda sorveteVenda) {
        try{
            Connection con = conexao();
            PreparedStatement atualizar = con.prepareStatement(ATUALIZAR);

            String dataStr = FORMATADOR_SQL.format(sorveteVenda.getDataMov());
            atualizar.setInt(1, sorveteVenda.getSabor());
            atualizar.setDouble(2, sorveteVenda.getQuantidade());
            atualizar.setInt(3, sorveteVenda.getTipoMov());
            atualizar.setString(4, dataStr);
            atualizar.setDouble(5, sorveteVenda.getPreco());
            atualizar.executeQuery();
            atualizar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualiazar o sorvete de id: " + sorveteVenda.getId());
            System.exit(0);
        }
    }

    @Override
    public void apagarSorvete(int id) {
        try {
            Connection con = conexao();
            PreparedStatement apagar = con.prepareStatement(APAGAR);
            apagar.setInt(1, id);
            apagar.executeUpdate();
            apagar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao apagar a conta de id: " + id);
        }
    }

    @Override
    public List<SorveteVenda> buscarTodosOsSorvetes() {
        List<SorveteVenda> sorveteVendas = new ArrayList<>();

        try{
            Connection con = conexao();
            PreparedStatement buscarTodos = con.prepareStatement(BUSCAR_TODOS);
            ResultSet resultadoBusca = buscarTodos.executeQuery();

            while (resultadoBusca.next()) {
                SorveteVenda sorveteVenda = extraiSorveteVenda(resultadoBusca);
                sorveteVendas.add(sorveteVenda);
            }
            buscarTodos.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os sorvetes");
            System.exit(0);
        }
        return sorveteVendas;
    }

    private SorveteVenda extraiSorveteVenda(ResultSet resultadoBusca) throws SQLException, ParseException {
        SorveteVenda sorveteVenda = new SorveteVenda();
        sorveteVenda.setId(resultadoBusca.getInt(1));
        sorveteVenda.setSabor(resultadoBusca.getInt(2));
        sorveteVenda.setQuantidade(resultadoBusca.getDouble(3));
        sorveteVenda.setTipoMov(resultadoBusca.getInt(4));
        sorveteVenda.setDataMov(FORMATADOR.parse(resultadoBusca.getString(5))) ;
        sorveteVenda.setPreco(resultadoBusca.getDouble(6));

        return sorveteVenda;
    }

    @Override
    public SorveteVenda buscarUmSorvete(int id) {
        return null;
    }
}
