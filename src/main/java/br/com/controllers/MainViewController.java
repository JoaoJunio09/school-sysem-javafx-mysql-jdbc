package br.com.controllers;

import br.com.schoolsystem.Main;
import br.com.util.Alerts;
import br.com.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private Label labelUsuario;

    @FXML
    private Label labelTipo;

    @FXML
    private Button btInicio;

    @FXML
    private Button btAluno;

    @FXML
    private Button btProfessor;

    @FXML
    private Button btSecretaria;

    @FXML
    private Button btBoletimEscolar;

    @FXML
    private Button btSegestoes;

    @FXML
    private Button btConfiguracoes;

    @FXML
    private Button btSair;

    @FXML
    public void onBtInicio(ActionEvent event) {
        loadView("/br/com/view/MainView.fxml", (MainViewController controller) -> {
            //
        });
    }

    @FXML
    public void onBtSecretaria(ActionEvent event) {
        loadView("/br/com/view/SecretariaView.fxml", (SecretariaViewController controller) -> {
            //
        });
    }

    private <T> void loadView(String absoluteName, Consumer<T> initializing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            AnchorPane anchorPaneLoader = loader.load();

            AnchorPane anchorPane = (AnchorPane) Main.getMainScene().getRoot();
            AnchorPane anchorPaneContent = (AnchorPane) anchorPane.getChildren().get(1);

            anchorPaneContent.getChildren().add(anchorPaneLoader);

            anchorPaneLoader.setLayoutY(60);

            anchorPaneLoader.prefHeightProperty().bind(anchorPaneContent.heightProperty());
            anchorPaneLoader.prefWidthProperty().bind(anchorPaneContent.widthProperty());

            T controller = loader.getController();
            initializing.accept(controller);
        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Erro ao carregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void initializeNodes() {
        //
    }
}