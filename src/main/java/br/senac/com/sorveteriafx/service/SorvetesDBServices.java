package br.senac.com.sorveteriafx.service;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.repository.Sorvetes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.*;

public class SorvetesDBServices implements Sorvetes {
    final String USUARIO = "root";
    final String SENHA = "root";
    final String URL_BANCO = "jdbc:mysql://localhost:3306/senac_sorveteriafx";
    final String CLASS_DRIVER = "com.mysql.jdbc.Driver";
    final String INSERIR = "";
    final String BUSCAR_TODOS = "";
    final String ATUALIZAR = "";
    final String FORMATO_DATA = "dd/mm/yyyy";
    final SimpleDateFormat FORMATADOR = new SimpleDateFormat(FORMATO_DATA);

    private Connection conexao() {
        try {
            Class.forName(CLASS_DRIVER);
            return DriverManager.getConnection(CLASS_DRIVER, USUARIO, SENHA);
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
    public void salvarSorvete(Sorvete sorvete) {
        try{
            Connection con = conexao();

            PreparedStatement salvar = con.prepareStatement(INSERIR);
            String dateStr = FORMATADOR.format(sorvete.getDataEntrada());
            salvar.setString(1, sorvete.getSabor());
            salvar.setDouble(2, sorvete.getValor());
            salvar.setDouble(3, sorvete.getQuantidade());
            salvar.setString(4, dateStr);
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
    public void atualizarSorvete(Sorvete sorvete) {
        try{
            Connection con = conexao();
            PreparedStatement atualizar = con.prepareStatement(ATUALIZAR);

            String dataStr = FORMATADOR.format(sorvete.getDataEntrada());
            atualizar.setString(1, sorvete.getSabor());
            atualizar.setDouble(2, sorvete.getValor());
            atualizar.setDouble(3, sorvete.getQuantidade());
            atualizar.setString(4, dataStr);
            atualizar.executeQuery();
            atualizar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualiazar o sorvete de id: " + sorvete.getId());
            System.exit(0);
        }
    }

    @Override
    public void apagarSorvete(Sorvete sorvete) {

    }

    @Override
    public Double buscarQuantidadeSorvete(int id) {
        return null;
    }

    @Override
    public Double buscarSaldo(int id) {
        return null;
    }

    @Override
    public List<Sorvete> buscarTodosOsSorvetes() {
        List<Sorvete> sorvetes = new ArrayList<>();

        try{
            Connection con = conexao();
            PreparedStatement buscarTodos = con.prepareStatement(BUSCAR_TODOS);
            ResultSet resultadoBusca = buscarTodos.executeQuery();

            while (resultadoBusca.next()) {
                Sorvete sorvete = extraiSorvete(resultadoBusca);
                sorvetes.add(sorvete);
            }
            buscarTodos.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os sorvetes");
            System.exit(0);
        }
        return sorvetes;
    }

    private Sorvete extraiSorvete(ResultSet resultadoBusca) throws SQLException, ParseException {
        Sorvete sorvete = new Sorvete();
        sorvete.setId(resultadoBusca.getInt(1));
        sorvete.setSabor(resultadoBusca.getString(2));
        sorvete.setValor(resultadoBusca.getDouble(3));

        Date dataEntrada = FORMATADOR.parse(resultadoBusca.getString(4));

        sorvete.setDataEntrada(dataEntrada);

        return sorvete;
    }

    @Override
    public Sorvete buscarUmSorvete(int id) {
        return null;
    }
}
