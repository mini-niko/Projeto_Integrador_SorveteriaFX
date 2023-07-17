package br.senac.com.sorveteriafx.controller;

import br.senac.com.sorveteriafx.model.Sorvete;
import br.senac.com.sorveteriafx.model.SorveteVenda;
import br.senac.com.sorveteriafx.repository.Sorvetes;
import br.senac.com.sorveteriafx.repository.SorvetesVenda;
import br.senac.com.sorveteriafx.service.SorvetesDBServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class viewController implements Initializable {
    @FXML
    private TableView<SorveteVenda> tblSorveteVenda;
    @FXML
    private TableColumn<SorveteVenda, String> colSabor;
    @FXML
    private TableColumn<SorveteVenda, String> colTipoMov;
    @FXML
    private TableColumn<SorveteVenda, String> colQuantidade;
    @FXML
    private TableColumn<SorveteVenda, String> colDataVenda;
    @FXML
    private TableColumn<SorveteVenda, String> colValor;

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

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnApagar;
    @FXML
    private Button btnLimpar;

    private SorvetesVenda sorvetesVendas;
    private Sorvetes sorvetes;

    private List<String> tipoMovList = new ArrayList<String>(Arrays.asList("Venda", "Compra", "Perda"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sorvetes = Sorvetes.getNewInstance();
        sorvetesVendas = SorvetesVenda.getNewInstance();

        cbSabor.setItems(FXCollections.observableList(sorvetes.buscarTodosOsSabores()));
        cbTipoMov.setItems(FXCollections.observableList(tipoMovList));

        dpDataVenda.setEditable(false);

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
                        txtQuantidade.setText(n.getQuantidade().toString());
                        dpDataVenda.setValue(data);
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
        tblSorveteVenda.getItems().setAll(sorvetesVendas.buscarTodosOsSorvetes());
    }
}
