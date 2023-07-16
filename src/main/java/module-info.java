module br.senac.com.sorveteriafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.senac.com.sorveteriafx.controller to javafx.fxml;
    exports br.senac.com.sorveteriafx.controller;

    opens br.senac.com.sorveteriafx.model to javafx.fxml;
    exports br.senac.com.sorveteriafx.model;

    opens br.senac.com.sorveteriafx.repository to javafx.fxml;
    exports br.senac.com.sorveteriafx.repository;

    opens br.senac.com.sorveteriafx.service to javafx.fxml;
    exports br.senac.com.sorveteriafx.service;

    opens br.senac.com.sorveteriafx.view to javafx.fxml;
    exports br.senac.com.sorveteriafx.view;
}