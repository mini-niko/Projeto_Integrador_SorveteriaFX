package br.senac.com.sorveteriafx.service;

import br.senac.com.sorveteriafx.repository.SaldoInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SaldoDBService implements SaldoInterface {
    final String USUARIO = "root";
    final String SENHA = "root";
    final String URL_BANCO = "jdbc:mysql://localhost:3306/senac_sorveteriafx";
    final String CLASS_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String BUSCAR_SALDO = "SELECT quantia FROM saldo";
    final String ALTERAR_SALDO = "UPDATE saldo SET quantia = ?";

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

    public double buscarSaldo() {
        double quantia = 0.0;

        try{
            Connection con = conexao();
            PreparedStatement buscar = con.prepareStatement(BUSCAR_SALDO);
            ResultSet resultado = buscar.executeQuery();
            resultado.next();
            quantia = resultado.getDouble(1);

            buscar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar saldo no banco de dados!");
            System.exit(0);
        }

        return quantia;
    }

    public void atualizarSaldo(double valorParaAtualizar) {
        try {
            Double valorAntigo = buscarSaldo();
            Double valorNovo = valorAntigo + valorParaAtualizar;

            Connection con = conexao();
            PreparedStatement atualizar = con.prepareStatement(ALTERAR_SALDO);
            atualizar.setDouble(1, valorNovo);
            atualizar.executeUpdate();

            atualizar.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar o saldo no banco de dados");
        }
    }
}
