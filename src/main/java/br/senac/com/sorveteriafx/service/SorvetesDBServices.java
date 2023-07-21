package br.senac.com.sorveteriafx.service;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.repository.Sorvetes;

import java.sql.*;
import java.util.ArrayList;

public class SorvetesDBServices implements Sorvetes {

    final String USUARIO = "root";
    final String SENHA = "root";
    final String URL_BANCO = "jdbc:mysql://localhost:3306/senac_sorveteriafx";
    final String CLASS_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String INSERIR = "INSERT INTO sorvete(quantidade, sabor) VALUES(0.0, ?)";
    final String BUSCAR_TODOS = "SELECT * FROM sorvete";
    final String BUSCAR_UM_SABOR = "SELECT * FROM sorvete WHERE id = ?";
    final String APAGAR = "DELETE FROM sorvete WHERE id = ?";
    final String ALTERAR_QUANTIDADE = "UPDATE sorvete SET quantidade = ? WHERE id = ?";

    private Connection conexao() {
        try {
            Class.forName(CLASS_DRIVER);
            return DriverManager.getConnection(URL_BANCO, USUARIO, SENHA);
        }
        catch (Exception e) {
            e.printStackTrace();
            if(e instanceof ClassNotFoundException) {
                System.out.println("Verifique se o Driver do Banco de Dados está carregado corretamente");
            }
            else {
                System.out.println("Verifique se o Banco de Dados está rodando e se os dados de conexão estão corretos");
            }
            System.exit(0);
            return null;
        }
    }

    @Override
    public void alterarQuantidadeSorvete(int id, Double quantidadeParaAlterar) {
        try {
            Double quantidadeAntiga = pegaQuantidade(id);
            Double quantidadeAtual = quantidadeAntiga + quantidadeParaAlterar;

            Connection con = conexao();
            PreparedStatement alterar = con.prepareStatement(ALTERAR_QUANTIDADE);
            alterar.setDouble(1, quantidadeAtual);
            alterar.setInt(2, id);

            alterar.executeUpdate();
            con.close();
            alterar.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao alterar o estoque do sabor de id: " + id);
            System.exit(0);
        }
    }

    @Override
    public void adicionarSorvete(String sabor) {
        try {
            Connection con = conexao();

            PreparedStatement adicionar = con.prepareStatement(INSERIR);
            adicionar.setString(1, sabor);
            adicionar.executeUpdate();
            con.close();
            adicionar.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.printf(String.format("Erro ao adicionar o sabor: '%s'.", sabor));
            System.exit(0);
        }
    }

    @Override
    public void removerSabor(int id) {
        try {
            Connection con = conexao();

            PreparedStatement remover = con.prepareStatement(APAGAR);
            remover.setInt(1, id);
            remover.executeUpdate();
            con.close();
            remover.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao remover o sabor de id: " + id);
            System.exit(0);
        }
    }


    @Override
    public ArrayList<Sorvete> buscarTodosOsSorvetes() {
        ArrayList<Sorvete> sorvetes = new ArrayList<>();

        try {
            Connection con = conexao();

            PreparedStatement buscarTodos = con.prepareStatement(BUSCAR_TODOS);
            ResultSet resultadoBusca = buscarTodos.executeQuery();

            while (resultadoBusca.next()) {
                Sorvete sorvete = extraiSorvete(resultadoBusca);
                sorvetes.add(sorvete);
            }
            con.close();
            buscarTodos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os sabores de sorvete.");
            System.exit(0);
        }

        return sorvetes;
    }

    @Override
    public ArrayList<String> buscarTodosOsSabores() {
        ArrayList<String> sabores = new ArrayList<>();

        try {
            Connection con = conexao();

            PreparedStatement buscarTodos = con.prepareStatement(BUSCAR_TODOS);
            ResultSet resultadoBusca = buscarTodos.executeQuery();

            while (resultadoBusca.next()) {
                String sabor = resultadoBusca.getString(3);
                sabores.add(sabor);
            }
            con.close();
            buscarTodos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os sabores de sorvete.");
            System.exit(0);
        }

        return sabores;
    }


    @Override
    public Sorvete buscarUmSorvete(int id) {
        Sorvete sorvete = new Sorvete();
        try {
            Connection con = conexao();
            PreparedStatement buscarUm = con.prepareStatement(BUSCAR_UM_SABOR);
            buscarUm.setInt(1, id);
            ResultSet resultadoBusca = buscarUm.executeQuery();
            resultadoBusca.next();

            sorvete = extraiSorvete(resultadoBusca);
            buscarUm.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o sabor de id: " + id);
            System.exit(0);
        }

        return sorvete;
    }

    @Override
    public String buscarUmSabor(int id) {
        String sorvete = null;

        try {
            Connection con = conexao();
            PreparedStatement buscarUm = con.prepareStatement(BUSCAR_UM_SABOR);
            buscarUm.setInt(1, id);
            ResultSet resultadoBusca = buscarUm.executeQuery();

            resultadoBusca.next();
            sorvete = resultadoBusca.getString(3);
            buscarUm.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o sabor de id: " + id);
            System.exit(0);
        }

        return sorvete;
    }

    private Double pegaQuantidade(int id) {
        Double quantidade = null;

        try {
            Connection con = conexao();

            PreparedStatement pegarValor = con.prepareStatement(BUSCAR_UM_SABOR);
            pegarValor.setInt(1, id);
            ResultSet resultadoBusca = pegarValor.executeQuery();
            resultadoBusca.next();
            quantidade =  resultadoBusca.getDouble(2);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao pegar a quantidade de sabor de id: " + id);
            System.exit(0);
        }

        return quantidade;
    }

    private Sorvete extraiSorvete(ResultSet resultadoBusca) throws SQLException {
        Sorvete sorvete = new Sorvete();
        sorvete.setId(resultadoBusca.getInt(1));
        sorvete.setQuantidade(resultadoBusca.getDouble(2));
        sorvete.setSabor(resultadoBusca.getString(3));

        return sorvete;
    }
}
