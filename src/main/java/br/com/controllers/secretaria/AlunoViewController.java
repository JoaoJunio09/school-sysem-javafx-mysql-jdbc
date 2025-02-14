package br.com.controllers.secretaria;

import br.com.model.entities.AlunoContato;
import br.com.model.services.AlunoContatoService;
import br.com.model.services.AlunoService;
import br.com.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class AlunoViewController implements Initializable {

    @FXML
    private AnchorPane anchorPaneContent;

    @FXML
    private BorderPane borderPaneActual;

    @FXML
    private Button btMatricula;

    @FXML
    private Button btPresenca;

    @FXML
    private Button btListar;

    @FXML
    private Button btContato;

    @FXML
    private Button btHorarios;

    @FXML
    private Button btBoletim;

    @FXML
    public void onBtMatriculaAction() {
        borderPaneActual.setVisible(false);
        loadView("/br/com/view/secretaria/MatriculaAlunoView.fxml", (MatriculaAlunoViewController controller) -> {
            controller.setServices(new AlunoService(), new AlunoContatoService());
            controller.updateTableViewAluno();
            controller.updateNumberAlunos();
        });
    }

    private <T> void loadView(String absoluteName, Consumer<T> initializing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            BorderPane borderPane = loader.load();

            anchorPaneContent.getChildren().add(borderPane);

            AnchorPane.setTopAnchor(borderPane, 0.0);
            AnchorPane.setLeftAnchor(borderPane, 0.0);
            AnchorPane.setRightAnchor(borderPane, 0.0);
            AnchorPane.setBottomAnchor(borderPane,0.0);

            borderPaneActual = borderPane;

            T controller = loader.getController();
            initializing.accept(controller);
        }
        catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception" , "Erro ao carregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {

    }
}
