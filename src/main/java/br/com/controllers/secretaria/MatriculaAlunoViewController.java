package br.com.controllers.secretaria;

import br.com.listeners.DataChangedListener;
import br.com.model.dto.AlunoDTO;
import br.com.model.entities.Aluno;
import br.com.model.entities.AlunoContato;
import br.com.model.entities.AlunoMatricula;
import br.com.model.entities.Pessoa;
import br.com.model.services.*;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MatriculaAlunoViewController implements Initializable, DataChangedListener {

    // DEPÊNDENCIAS:
    private AlunoService alunoService;

    private AlunoContatoService alunoContatoService;

    @FXML
    private Label labelNumeroAlunos;

    // Panes SceneBuilder:
    @FXML
    private ScrollPane scrollPaneAluno;

    @FXML
    private AnchorPane anchorPaneTableView;

    @FXML
    private AnchorPane anchorPaneAlterar;

    // Tabela - Aluno:
    @FXML
    private TableView<AlunoDTO> tableViewAluno;

    @FXML
    private TableColumn<AlunoDTO, Integer> tableColumnId;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnNome;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnEmail;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnRg;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnSexo;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnSerie;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnTurma;

    @FXML
    private TableColumn<AlunoDTO, AlunoDTO> tableColumnEDIT;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnRa;

    // Buttons:
    @FXML
    private Button btMatricular;

    @FXML
    private Button btDesmatricular;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btHistórico;

    @FXML
    private Button btVoltar;

    @FXML
    public void onBtsLoadViewAluno(ActionEvent event) {
        if (event.getSource() == btAlterar) {
            anchorPaneTableView.setVisible(false);
            anchorPaneAlterar.setVisible(true);
        }
        if (event.getSource() == btVoltar) {
            anchorPaneTableView.setVisible(true);
            anchorPaneAlterar.setVisible(false);
        }
    }

    @FXML
    public void onBtMatricularAction(ActionEvent event) {
        createFormMatricular("/br/com/view/secretaria/MatricularAlunoView.fxml",
                new Aluno(), new Pessoa(), new AlunoMatricula(), new AlunoContato(), Utils.currentStage(event));
    }

    @FXML
    public void onBtDesmatricularAction(ActionEvent event) {
        createFormDesmatricular("/br/com/view/secretaria/DesmatricularAlunoView.fxml", Utils.currentStage(event));
    }

    private ObservableList<AlunoDTO> obsListAlunoDTO;

    public void setServices(AlunoService alunoService, AlunoContatoService alunoContatoService) {
        this.alunoService = alunoService;
        this.alunoContatoService = alunoContatoService;
    }

    private void createFormMatricular(String absoluteName, Aluno aluno, Pessoa pessoa, AlunoMatricula matricula, AlunoContato alunoContato, Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            MatricularAlunoViewController controller = loader.getController();
            controller.setEntities(aluno, pessoa, matricula, alunoContato);
            controller.setServices(new AlunoService(), new PessoaService(), new AlunoMatriculaService(), new AlunoContatoService(), new TurmaService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            controller.loadAssociatedAllComboBox();

            Stage stageForm = new Stage();
            stageForm.setTitle("Dados do aluno");
            stageForm.setScene(new Scene(pane));
            stageForm.setResizable(false);
            stageForm.initModality(Modality.APPLICATION_MODAL);
            stageForm.initOwner(parent);
            stageForm.showAndWait();
        }
        catch (IOException e) {
            Alerts.showAlert("Erro ao carregar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void createFormDesmatricular(String absoluteName, Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DesmatricularAlunoViewController controller = loader.getController();
            controller.setServices(new AlunoService(), new PessoaService(), new AlunoMatriculaService(), new AlunoContatoService());
            controller.subscribeDataChangedListener(this);

            Stage stageForm = new Stage();
            stageForm.setTitle("Desmatricular aluno");
            stageForm.setScene(new Scene(pane));
            stageForm.setResizable(false);
            stageForm.initModality(Modality.APPLICATION_MODAL);
            stageForm.initOwner(parent);
            stageForm.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro ao carregar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void updateTableViewAluno() {
        if (alunoService == null) {
            throw new IllegalStateException("Aluno Service was null");
        }

        obsListAlunoDTO = FXCollections.observableList(convertToDTO(alunoService.findAll()));
        tableViewAluno.setItems(obsListAlunoDTO);

        initEditButtons();
    }

    private List<AlunoDTO> convertToDTO(List<Aluno> list) {
        List<AlunoDTO> listDTO = new ArrayList<>();
        for (Aluno aluno : list) {
            listDTO.add(new AlunoDTO(aluno, aluno.getAluno_matricula()));
        }
        return listDTO;
    }

    public void updateNumberAlunos() {
        int counter = 0;
        for (Aluno aluno : alunoService.findAll()) {
            counter++;
        }
        labelNumeroAlunos.setText(String.valueOf(counter));
    }

    private void loadDetailsAluno(AlunoDTO objDTO, ActionEvent event) {
        Aluno obj = null;
        AlunoContato objContato = null;
        for (Aluno aluno : alunoService.findAll()) {
            if (aluno.getId().equals(objDTO.getId())) {
                obj = aluno;
            }
        }
        for (AlunoContato alunoContato : alunoContatoService.findAll()) {
            if (alunoContato.getAluno().getId().equals(obj.getId())) {
                objContato = alunoContato;
            }
        }

        createFormMatricular("/br/com/view/secretaria/MatricularAlunoView.fxml", obj, obj.getPessoa(), obj.getAluno_matricula(),objContato, Utils.currentStage(event));
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<AlunoDTO, AlunoDTO>() {
            private final Button button = new Button("Detalhes");

            @Override
            protected void updateItem(AlunoDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setPrefWidth(100);
                button.setStyle("-fx-text-fill: #000");
                button.setOnAction(
                        event -> loadDetailsAluno(obj, event));
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("serie"));
        tableColumnTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        tableColumnRa.setCellValueFactory(new PropertyValueFactory<>("RA"));

        scrollPaneAluno.setFitToHeight(true);
        scrollPaneAluno.setFitToWidth(true);

        tableViewAluno.prefWidthProperty().bind(scrollPaneAluno.prefWidthProperty());
        tableViewAluno.prefHeightProperty().bind(scrollPaneAluno.prefHeightProperty());

        anchorPaneTableView.setVisible(true);
        anchorPaneAlterar.setVisible(false);
    }

    @Override
    public void onDataChangedListener() {
        updateTableViewAluno();
        updateNumberAlunos();
    }
}