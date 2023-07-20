package br.senac.com.sorveteriafx.controller;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.model.SorveteMov;
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
    private List<String> tipoMovList = new ArrayList<String>(Arrays.asList("Venda", "Compra", "Perda"));

    //Abaixo estão os métodos utilizados para configuração da nossa aplicação, como tabela e dados

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sorvetes = Sorvetes.getNewInstance();
        sorvetesMov = SorvetesMov.getNewInstance();

        cbSabor.setItems(FXCollections.observableList(sorvetes.buscarTodosOsSabores()));
        cbTipoMov.setItems(FXCollections.observableList(tipoMovList));

        dpDataVenda.setEditable(false);
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

                        System.out.println(String.format(Locale.US, "%.2f", n.getPreco()));

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
    }

    private void atualizaDados() {
        tblSorveteVenda.getItems().setAll(sorvetesMov.buscarTodosOsSorvetesMov());
    }

    //Abaixo estão os métodos utilizados para trabalhar com os campos de inserção de dados

    public void pegaValores(SorveteMov sorveteMov) {

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

    //Abaixo estão os métodos ativados por botões
    @FXML
    private void salvar() {
        SorveteMov sorveteMov = new SorveteMov();
        pegaValores(sorveteMov);

        sorvetesMov.salvarSorveteMov(sorveteMov);
        atualizaDados();
    }

    @FXML
    private void atualizar() {
        SorveteMov sorveteMov = tblSorveteVenda.getSelectionModel().selectedItemProperty().getValue();
        pegaValores(sorveteMov);
        sorvetesMov.atualizarSorveteMov(sorveteMov);

        atualizaDados();
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
        int id = tblSorveteVenda.getSelectionModel().selectedItemProperty().getValue().getId();
        sorvetesMov.apagarSorveteMov(id);
        atualizaDados();
    }
}
