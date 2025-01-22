package br.com.controllers;

import br.com.listeners.DataChangedListener;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainSecretariaViewController implements Initializable, DataChangedListener {

    private Usuario entityUser;

    private AlunoService alunoService;

    private AlunoContatoService alunoContatoService;

    @FXML
    private BorderPane borderPaneInicio;

    @FXML
    private BorderPane borderPaneAluno;

    @FXML
    private TableView<AlunoDTO> tableViewAluno;

    @FXML
    private TableColumn<AlunoDTO, Integer> tableColumnId;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnNome;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnEmail;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnRG;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnSexo;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnSerie;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnTurma;

    @FXML
    private TableColumn<AlunoDTO, String> tableColumnRA;

    @FXML
    private TableColumn<AlunoDTO, AlunoDTO> tableColumnDETALHES;

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
    private Button btMatricularAluno;

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
    private ScrollPane scrollPaneRecentes;

    @FXML
    private ScrollPane scrollPaneTableView;

    @FXML
    private VBox VBoxScrollPaneRecentes;

    @FXML
    private Label labelQuantidadeAlunos;

    @FXML
    public void onBtMatricularAluno(ActionEvent event) {
        Stage parent = Utils.currentStage(event);
        createDialogForm(new Aluno(), new Pessoa(), new AlunoMatricula(), new AlunoContato(), "/br/com/view/MatricularAlunoView.fxml", parent);
    }

    @FXML
    public void onBtLoadMainSecretariaAction(ActionEvent event) {
        if (event.getSource() == btInicio) {
            borderPaneInicio.setVisible(true);
            borderPaneAluno.setVisible(false);
        }
        if (event.getSource() == btAluno) {
            borderPaneInicio.setVisible(false);
            borderPaneAluno.setVisible(true);
        }
    }

    private ObservableList<AlunoDTO> obsList;

    public void setEntityUser(Usuario entityUser) {
        this.entityUser = entityUser;
    }

    public void setServices(AlunoService alunoService, AlunoContatoService alunoContatoService) {
        this.alunoService = alunoService;
        this.alunoContatoService = alunoContatoService;
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

    private void createDialogForm(Aluno objAluno, Pessoa objPessoa, AlunoMatricula objAlunoMatricula, AlunoContato objAlunoContato, String absoluteName, Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            MatricularAlunoViewController controller = loader.getController();
            controller.setEntities(objAluno, objPessoa, objAlunoMatricula, objAlunoContato);
            controller.setServices(new AlunoService(), new PessoaService(), new AlunoMatriculaService(), new AlunoContatoService(), new TurmaService());
            controller.setDataChangedListener(this);
            controller.loadAssociatedAllComboBox();
            controller.updateFormData();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.initOwner(parent);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception" , "Erro ao carregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void updateTableView() {
        if (alunoService == null) {
            throw new IllegalStateException("Service was null");
        }

        List<AlunoDTO> list = new ArrayList<>();
        for (Aluno aluno : alunoService.findAll()) {
            list.add(new AlunoDTO(aluno, aluno.getAluno_matricula()));
        }

        obsList = FXCollections.observableArrayList(list);
        tableViewAluno.setItems(obsList);

        initDetalhesButtons();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        borderPaneInicio.setVisible(true);
        borderPaneAluno.setVisible(false);

        VBoxScrollPaneRecentes.prefWidthProperty().bind(scrollPaneRecentes.widthProperty());

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnRG.setCellValueFactory(new PropertyValueFactory<>("rg"));
        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("serie"));
        tableColumnTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        tableColumnRA.setCellValueFactory(new PropertyValueFactory<>("RA"));
    }

    private void exibirDetalhesAluno(AlunoDTO obj, ActionEvent event) {
        Aluno aluno = new Aluno(obj.getId(), obj.getPessoa(), obj.getAlunoMatricula());

        AlunoContato contato = null;

        List<AlunoContato> list = alunoContatoService.findAll();
        for (AlunoContato alunoContato : list) {
            if (alunoContato.getAluno().getId().equals(obj.getId())) {
                contato = alunoContato;
            }
        }

        createDialogForm(aluno, aluno.getPessoa(), aluno.getAluno_matricula(), contato, "/br/com/view/MatricularAlunoView.fxml", Utils.currentStage(event));
    }

    private void initDetalhesButtons() {
        tableColumnDETALHES.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDETALHES.setCellFactory(param -> new TableCell<AlunoDTO, AlunoDTO>() {
            private final Button button = new Button("Detalhes");

            @Override
            protected void updateItem(AlunoDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> exibirDetalhesAluno(obj, event));
            }
        });
    }

    @Override
    public void onDataChangedListener() {
        updateTableView();
    }
}