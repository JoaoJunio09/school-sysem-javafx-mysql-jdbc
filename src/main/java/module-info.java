module br.com.schoolsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.com.schoolsystem to javafx.fxml;
    opens br.com.controllers to javafx.fxml;
    exports br.com.schoolsystem;
    exports br.com.controllers;
    exports br.com.model.dto;
    exports br.com.controllers.secretaria;
    opens br.com.controllers.secretaria to javafx.fxml;
}