package br.com.controllers.secretaria;

import br.com.exceptions.DbException;
import br.com.listeners.DataChangedListener;
import br.com.listeners.MonitorsRecentNew;
import br.com.model.dto.AlunoDTO;
import br.com.model.entities.*;
import br.com.model.services.*;
import br.com.schoolsystem.Main;
import br.com.util.Alerts;
import br.com.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainSecretariaViewController implements Initializable, MonitorsRecentNew {

    private Usuario entityUser;

    @FXML
    private AnchorPane anchorPaneContent;

    @FXML
    private BorderPane borderPaneActual;

    @FXML
    private Label labelUsuario;

    @FXML
    private Label labelTipo;

    @FXML
    private Label labelUsuarioBemVindo;

    @FXML
    private Button btInicio;

    @FXML
    private Button btAluno;

    @FXML
    private Button btProfessor;

    @FXML
    private Button btBoletim;

    @FXML
    private Button btIndices;

    @FXML
    private Button btAdministrativo;

    @FXML
    private Button btConfiguracoes;

    @FXML
    private Button btSair;

    @FXML
    public void onBtInicioAction(ActionEvent event) {
        borderPaneActual.setVisible(false);
        loadView("/br/com/view/secretaria/InicioView.fxml", (InicioViewController controller) -> {
            //
        });
    }

    @FXML
    public void onBtAlunoAction(ActionEvent event) {
        borderPaneActual.setVisible(false);
        loadView("/br/com/view/secretaria/AlunoView.fxml", (AlunoViewController controller) -> {
            controller.setServices(new AlunoService(), new AlunoContatoService());
            controller.updateTableView();
        });
    }

    public void setEntityUser(Usuario entityUser) {
        this.entityUser = entityUser;
    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            BorderPane borderPane = loader.load();

            anchorPaneContent.getChildren().add(borderPane);

            borderPane.setLayoutY(50);

            AnchorPane.setTopAnchor(borderPane, 50.0);
            AnchorPane.setLeftAnchor(borderPane, 0.0);
            AnchorPane.setRightAnchor(borderPane, 0.0);
            AnchorPane.setBottomAnchor(borderPane,0.0);

            borderPaneActual = borderPane;

            T controller = loader.getController();
            initializing.accept(controller);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDataEntityUser() {
        if (entityUser == null) {
            throw new IllegalStateException("EntityUser was null");
        }

        labelUsuario.setText(entityUser.getEmail());
        labelUsuarioBemVindo.setText(entityUser.getEmail());
        labelTipo.setText(identityUserTypeNumber(entityUser.getTipoUsuario()));
    }

    private String identityUserTypeNumber(Integer numberType) {
        String valueString = "";
        switch (numberType) {
            case 1:
                valueString = "Secretaria";
                break;
            case 2:
                valueString = "Aluno";
                break;
            case 3:
                valueString = "Professor";
                break;
            case 4:
                valueString = "Gestão Escolar";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + numberType);
        }

        return valueString;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        loadView("/br/com/view/secretaria/InicioView.fxml", (InicioViewController controller) -> {
            //
        });
    }

    @Override
    public void onMonitorsRecentNew(String message) {

    }
}