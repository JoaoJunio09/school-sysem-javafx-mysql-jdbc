package br.com.schoolsystem;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.Pessoa;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class HelloApplication extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/br/com/view/MainView.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        mainScene = new Scene(anchorPane);
        stage.setTitle("School System - application");
        stage.setScene(mainScene);
        stage.show();
        */

        CRUD<Pessoa> pessoaDao = DaoFactory.createPessoaDaoJDBC();

        Pessoa pessoa = new Pessoa(null, "João Junio", "R. Marechal Castelo Branco", "Casa", "353", "Centro", "15180000", "joaojunio818@gmail.com", new Date(), "M", "12345678910", "123456789101", "Paramirim(BA)", "Brasil");
    }

    public static Scene getMainScene() { return mainScene; }

    public static void main(String[] args) {
        launch();
    }
}