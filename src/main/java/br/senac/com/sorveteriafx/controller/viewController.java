package br.senac.com.sorveteriafx.controller;

import br.senac.com.sorveteriafx.model.SorveteVenda;
import br.senac.com.sorveteriafx.repository.SorvetesVenda;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class viewController implements Initializable {
    @FXML
    private TableView<SorveteVenda> tblSorveteVenda;
    @FXML
    private TableColumn<SorveteVenda, String> colSabor;
    @FXML
    private TableColumn<SorveteVenda, Integer> colTipoMov;
    @FXML
    private TableColumn<SorveteVenda, Double> colQuantidade;
    @FXML
    private TableColumn<SorveteVenda, String> colDataVenda;
    @FXML
    private TableColumn<SorveteVenda, Double> colValor;

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

    private SorvetesVenda sorvetes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sorvetes = SorvetesVenda.getNewInstance();

        cbSabor.getItems().add("Napolitano");

        dpDataVenda.setEditable(false);
    }

    private void configuraTabelas() {
        tblSorveteVenda.getSelectionModel().selectedItemProperty().addListener(
                (b, o, n) -> {
                    if(n != null) {
                        LocalDate data = null;
                        data = n.getDataMov().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


                        //cbSabor.setItems();
                    }
                }
        );
    }
}
