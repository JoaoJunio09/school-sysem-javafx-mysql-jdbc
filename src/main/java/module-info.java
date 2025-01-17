module br.com.schoolsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens br.com.schoolsystem to javafx.fxml;
    opens br.com.controllers to javafx.fxml;
    exports br.com.schoolsystem;
    exports br.com.controllers;
}