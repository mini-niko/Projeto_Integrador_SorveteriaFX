package br.senac.com.sorveteriafx.controller;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.model.SorveteMov;
import br.senac.com.sorveteriafx.repository.SaldoInterface;
import br.senac.com.sorveteriafx.repository.Sorvetes;
import br.senac.com.sorveteriafx.repository.SorvetesMov;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class viewController implements Initializable {

    //Abaixo estão as nossas variáveis para a tabela de movimento
    @FXML
    private TableView<SorveteMov> tblSorveteVenda;
    @FXML
    private TableColumn<SorveteMov, String> colSabor;
    @FXML
    private TableColumn<SorveteMov, String> colTipoMov;
    @FXML
    private TableColumn<SorveteMov, String> colQuantidade;
    @FXML
    private TableColumn<SorveteMov, String> colDataVenda;
    @FXML
    private TableColumn<SorveteMov, String> colValor;

    //Abaixo estão as variáveis para a tabela de saldo de sorvetes
    @FXML
    private TableView<Sorvete> tblSorveteEstoque;
    @FXML
    private TableColumn<Sorvete, String> colSaborEstoque;
    @FXML
    private TableColumn<Sorvete, String> colQuantEstoque;
    @FXML
    private TextField txtSaldo;

    //Abaixo estão as variáveis dos nossos campos de inserção de dados

    @FXML
    private ChoiceBox<String> cbSabor;
    @FXML
    private ChoiceBox<String> cbTipoMov;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private DatePicker dpDataVenda;
    @FXML
    private TextField txtValor;

    //Abaixo estão nossas variáveis dos nossos botões

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnApagar;
    @FXML
    private Button btnLimpar;

    //Abaixo estão nossas variáveis de modelos utilizados

    private SorvetesMov sorvetesMov;
    private Sorvetes sorvetes;
    private SaldoInterface saldo;
    private List<String> tipoMovList = new ArrayList<String>(Arrays.asList("Venda", "Compra"));

    //Abaixo estão os métodos utilizados para configuração da nossa aplicação, como tabela e dados

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sorvetes = Sorvetes.getNewInstance();
        sorvetesMov = SorvetesMov.getNewInstance();
        saldo = SaldoInterface.getNewInstance();

        cbSabor.setItems(FXCollections.observableList(sorvetes.buscarTodosOsSabores()));
        cbTipoMov.setItems(FXCollections.observableList(tipoMovList));

        dpDataVenda.setEditable(false);
        txtSaldo.setEditable(false);

        txtQuantidade.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        txtValor.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        configuraTela();
        configuraColunas();
        atualizaDados();
    }

    private void configuraTela() {
        tblSorveteVenda.getSelectionModel().selectedItemProperty().addListener(
                (b, o, n) -> {
                    if(n != null) {
                        LocalDate data = null;
                        data = n.getDataMov().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        cbSabor.setValue(sorvetes.buscarUmSabor(n.getSabor()));
                        cbTipoMov.setValue(tipoMovList.get(n.getTipoMov()));
                        txtQuantidade.setText(Double.toString(n.getPreco()).replace(".", ","));
                        dpDataVenda.setValue(data);
                        txtValor.setText(Double.toString(n.getPreco()).replace(".", ","));
                    }
                }
        );
    }

    private void configuraColunas() {
        colSabor.setCellValueFactory(cellData -> {
            int saborId = cellData.getValue().getSabor();
            String saborTxt = sorvetes.buscarUmSabor(saborId);
            return new SimpleStringProperty(saborTxt);
        });
        colTipoMov.setCellValueFactory(cellData -> {
            int tipoMovId = cellData.getValue().getTipoMov();
            String movIdTxt = tipoMovList.get(tipoMovId);

            return new SimpleStringProperty(movIdTxt);
        });
        colQuantidade.setCellValueFactory(cellData -> {
            Double quantidade = cellData.getValue().getQuantidade();
            String quantidadeTxt = String.format("%.3fL", quantidade);
            return new SimpleStringProperty(quantidadeTxt);
        });
        colDataVenda.setCellValueFactory(cellData -> {
            Date data = cellData.getValue().getDataMov();
            String dataFormada = new SimpleDateFormat("dd/MM/yyyy").format(data);

            return new SimpleStringProperty(dataFormada);
        });
        colValor.setCellValueFactory(cellData -> {
            Double preco = cellData.getValue().getPreco();
            String precoTxt = String.format("R$%.2f", preco).replace(".", ",");

            return new SimpleStringProperty(precoTxt);
        });

        colSaborEstoque.setCellValueFactory(cellData -> {
            String sabor = cellData.getValue().getSabor();
            return new SimpleStringProperty(sabor);
        });
        colQuantEstoque.setCellValueFactory(cellData -> {
            Double quantidade = cellData.getValue().getQuantidade();
            String quantidadeTxt = String.format("%.3fL", quantidade);
            return new SimpleStringProperty(quantidadeTxt);
        });
    }

    private void atualizaDados() {
        tblSorveteVenda.getItems().setAll(sorvetesMov.buscarTodosOsSorvetesMov());
        tblSorveteEstoque.getItems().setAll(sorvetes.buscarTodosOsSorvetes());
        if(saldo.buscarSaldo() < 0)
            txtSaldo.setText(String.format("-R$%.2f", (saldo.buscarSaldo() * -1)).replace(".", ","));
        else
            txtSaldo.setText(String.format("R$%.2f", saldo.buscarSaldo()).replace(".", ","));
    }

    //Abaixo estão os métodos utilizados para trabalhar com os campos de inserção de dados

    private void pegaValores(SorveteMov sorveteMov) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        dfs.setDecimalSeparator('.');

        DecimalFormat dfValor = new DecimalFormat("########.00", dfs);
        DecimalFormat dfQuantidade = new DecimalFormat("########.000", dfs);

        int saborId = pegaIdSabor(cbSabor.getValue());
        int movTipoId = tipoMovList.indexOf(cbTipoMov.getValue());
        Double quantidade = Double.valueOf(txtQuantidade.getText().replace(".", "").replaceFirst(",", "."));
        quantidade = Double.valueOf(dfQuantidade.format(quantidade));
        Date dataMov = pegaData();
        Double valor = Double.valueOf(txtValor.getText().replace(".", "").replaceFirst(",", "."));
        valor = Double.valueOf(dfValor.format(valor));

        sorveteMov.setSabor(saborId);
        sorveteMov.setTipoMov(movTipoId);
        sorveteMov.setQuantidade(quantidade);
        sorveteMov.setDataMov(dataMov);
        sorveteMov.setPreco(valor);
    }

    private Date pegaData() {
        LocalDateTime time = dpDataVenda.getValue().atStartOfDay();
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    private int pegaIdSabor(String sabor) {
        int id = -1;
        List<Sorvete> sorveteList = sorvetes.buscarTodosOsSorvetes();

        for(Sorvete sorvete : sorveteList) {
            if(sorvete.getSabor().matches(sabor)) {
                id = sorvete.getId();
            }
        }
        return id;
    }

    private void chamarAlerta(Alert.AlertType tipoAlerta , String titulo, String subtitulo, String mensagem) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(subtitulo);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }

    private boolean chamarAlertaConfirmacao(String titulo, String subtitulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(subtitulo);
        alerta.setContentText(mensagem);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if(resultado.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    private boolean camposCheios() {
        if(cbSabor.getValue() == null) return false;
        if(cbTipoMov.getValue() == null) return false;
        if(txtQuantidade.getText().isEmpty() || Double.valueOf(txtQuantidade.getText()) <= 0.0  ) return false;
        if(txtValor.getText().isEmpty() || Double.valueOf(txtValor.getText()) <= 0.0) return false;
        if((dpDataVenda.getValue()) == null) return false;

        return true;
    }

    private boolean sorveteMovSelecionado() {
        if(tblSorveteVenda.getSelectionModel().isEmpty()) return false;
        return true;
    }

    //Abaixo estão os métodos ativados por botões
    @FXML
    private void salvar() {
        if(camposCheios()) {
            SorveteMov sorveteMov = new SorveteMov();
            pegaValores(sorveteMov);

            Double quantidadeAntiga = sorvetes.buscarUmSorvete(sorveteMov.getSabor()).getQuantidade();
            Double quantidadeNova = sorveteMov.getQuantidade();

            if((quantidadeAntiga - quantidadeNova) < 0 && sorveteMov.getTipoMov() == 0) {
                chamarAlerta(Alert.AlertType.ERROR, "Erro ao salvar movimento", "Quantidade inserida é maior que a disponível no estoque","Diminua a quantidade do sorvete no movimento.");
            }
            else {
                switch (sorveteMov.getTipoMov()) {
                    case 0:
                        if((quantidadeAntiga - quantidadeNova) < 0) {
                            chamarAlerta(Alert.AlertType.ERROR, "Erro ao salvar movimento", "Quantidade inserida é maior que a disponível no estoque","Diminua a quantidade do sorvete no movimento.");
                        }
                        else {
                            saldo.atualizarSaldo(sorveteMov.getPreco());
                            sorvetes.alterarQuantidadeSorvete(sorveteMov.getSabor(), (sorveteMov.getQuantidade() * -1));
                        }
                        break;
                    case 1:
                        //Saldo -, Estoque+
                        saldo.atualizarSaldo((sorveteMov.getPreco() * -1));
                        sorvetes.alterarQuantidadeSorvete(sorveteMov.getSabor(), (sorveteMov.getQuantidade()));
                        break;
                }
                sorvetesMov.salvarSorveteMov(sorveteMov);
                atualizaDados();
            }
        }
        else {
            chamarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao salvar movimento",
                    "Falta de informações",
                    "Preencha todas as informações corretamente"
            );
        }

    }

    @FXML
    private void atualizar() {
        if(camposCheios() && sorveteMovSelecionado()) {
            SorveteMov sorveteMov = tblSorveteVenda.getSelectionModel().selectedItemProperty().getValue();
            pegaValores(sorveteMov);
            sorvetesMov.atualizarSorveteMov(sorveteMov);

            atualizaDados();
        }
        else if(!(sorveteMovSelecionado())) {
            chamarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao atualizar movimento",
                    "Movimento não selecionado",
                    "Selecione um movimento para atualizar"
            );
        }
        else if(!(camposCheios())) {
            chamarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao atualizar movimento",
                    "Falta de informações",
                    "Preencha todas as informações corretamente!"
            );
        }
    }

    @FXML
    private void limpar() {
        tblSorveteVenda.getSelectionModel().clearSelection();
        cbSabor.setValue(null);
        cbTipoMov.setValue(null);
        txtQuantidade.setText("");
        dpDataVenda.setValue(null);
        txtValor.setText("");
    }

    @FXML
    private void apagar() {
        if(sorveteMovSelecionado()) {
            boolean confirmar = chamarAlertaConfirmacao("Confirmação", "Confirmação para apagar movimento", "Você realmente deseja apagar este movimento? (Esta ação não poderá ser desfeita)");
            if(confirmar) {
                SorveteMov sorveteMov = tblSorveteVenda.getSelectionModel().selectedItemProperty().getValue();
                switch (sorveteMov.getTipoMov()) {
                    case 0:
                        saldo.atualizarSaldo((sorveteMov.getPreco() * -1));
                        sorvetes.alterarQuantidadeSorvete(sorveteMov.getSabor(), (sorveteMov.getQuantidade()));
                        break;
                    case 1:
                        saldo.atualizarSaldo(sorveteMov.getPreco());
                        sorvetes.alterarQuantidadeSorvete(sorveteMov.getSabor(), (sorveteMov.getQuantidade() * -1));
                        break;
                }
                sorvetesMov.apagarSorveteMov(sorveteMov.getId());
                atualizaDados();
            }
        }
        else {
            chamarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao apagar conta",
                    "Movimento não selecionado",
                    "Selecione um movimento para apagá-lo"
            );
        }
    }
}
