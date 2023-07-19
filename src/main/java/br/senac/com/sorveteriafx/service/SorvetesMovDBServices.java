package br.senac.com.sorveteriafx.service;

import br.senac.com.sorveteriafx.model.SorveteMov;
import br.senac.com.sorveteriafx.repository.SorvetesMov;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SorvetesMovDBServices implements SorvetesMov {
    final String USUARIO = "root";
    final String SENHA = "root";
    final String URL_BANCO = "jdbc:mysql://localhost:3306/senac_sorveteriafx";
    final String CLASS_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String INSERIR = "INSERT INTO sorvete_mov(id_sabor, quantidade, tipo_mov, data_mov, preco) VALUES(?, ?, ?, ?, ?)";
    final String BUSCAR = "SELECT * FROM sorvete_mov WHERE id = ?";
    final String BUSCAR_TODOS = "SELECT id, id_sabor, quantidade, tipo_mov, DATE_FORMAT(data_mov, '%d/%m/%Y'), preco FROM sorvete_mov";
    final String APAGAR = "DELETE FROM sorvete_mov WHERE id = ?";
    final String ATUALIZAR = "UPDATE sorvete_mov SET id_sabor = ?, quantidade = ?, tipo_mov = ?, data_mov = ?, preco = ? WHERE id = ?;";
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
    public void salvarSorveteMov(SorveteMov sorveteMov) {
        try{
            Connection con = conexao();

            PreparedStatement salvar = con.prepareStatement(INSERIR);
            String dateStr = FORMATADOR_SQL.format(sorveteMov.getDataMov());
            salvar.setInt(1, sorveteMov.getSabor());
            salvar.setDouble(2, sorveteMov.getQuantidade());
            salvar.setInt(3, sorveteMov.getTipoMov());
            salvar.setString(4, dateStr);
            salvar.setDouble(5, sorveteMov.getPreco());
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
    public void atualizarSorveteMov(SorveteMov sorveteMovNovo) {
        try{
            Connection con = conexao();

            PreparedStatement salvar = con.prepareStatement(ATUALIZAR);
            String dateStr = FORMATADOR_SQL.format(sorveteMovNovo.getDataMov());
            salvar.setInt(1, sorveteMovNovo.getSabor());
            salvar.setDouble(2, sorveteMovNovo.getQuantidade());
            salvar.setInt(3, sorveteMovNovo.getTipoMov());
            salvar.setString(4, dateStr);
            salvar.setDouble(5, sorveteMovNovo.getPreco());
            salvar.setInt(6, sorveteMovNovo.getId());
            salvar.executeUpdate();
            salvar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar o sorvete de id: " + sorveteMovNovo.getId());
            System.exit(0);
        }
    }

    @Override
    public void apagarSorveteMov(int id) {
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
    public List<SorveteMov> buscarTodosOsSorvetesMov() {
        List<SorveteMov> sorveteMovs = new ArrayList<>();

        try{
            Connection con = conexao();
            PreparedStatement buscarTodos = con.prepareStatement(BUSCAR_TODOS);
            ResultSet resultadoBusca = buscarTodos.executeQuery();

            while (resultadoBusca.next()) {
                SorveteMov sorveteMov = extraiSorveteMov(resultadoBusca);
                sorveteMovs.add(sorveteMov);
            }
            buscarTodos.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os sorvetes");
            System.exit(0);
        }
        return sorveteMovs;
    }

    private SorveteMov extraiSorveteMov(ResultSet resultadoBusca) throws SQLException, ParseException {
        SorveteMov sorveteMov = new SorveteMov();
        sorveteMov.setId(resultadoBusca.getInt(1));
        sorveteMov.setSabor(resultadoBusca.getInt(2));
        sorveteMov.setQuantidade(resultadoBusca.getDouble(3));
        sorveteMov.setTipoMov(resultadoBusca.getInt(4));
        sorveteMov.setDataMov(FORMATADOR.parse(resultadoBusca.getString(5))) ;
        sorveteMov.setPreco(resultadoBusca.getDouble(6));

        return sorveteMov;
    }

    @Override
    public SorveteMov buscarUmSorveteMov(int id) {
        SorveteMov sorveteMov = null;

        try {
            Connection con = conexao();

            PreparedStatement buscar = con.prepareStatement(BUSCAR);
            buscar.setInt(1, id);
            sorveteMov = extraiSorveteMov(buscar.executeQuery());
            buscar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o sorveteMov de id: " + id);
        }

        return sorveteMov;
    }
}
