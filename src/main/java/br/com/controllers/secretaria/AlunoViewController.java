package br.com.controllers.secretaria;

import br.com.exceptions.DbException;
import br.com.model.dto.AlunoDTO;
import br.com.model.entities.*;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlunoViewController implements Initializable {

    private Usuario entityUser;

    private AlunoService alunoService;

    private AlunoContatoService alunoContatoService;

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private AnchorPane anchorPaneParent;

    @FXML
    private TextField txtBuscaPorNome;

    @FXML
    private TextField txtBuscaPorRa;

    @FXML
    private Button btBuscarAluno;

    @FXML
    private Button btListarTodosAlunos;

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
    public void onBtMatricularAlunoAction(ActionEvent event) {
        Stage parent = Utils.currentStage(event);
        createDialogForm(new Aluno(), new Pessoa(), new AlunoMatricula(), new AlunoContato(), "/br/com/view/secretaria/MatricularAlunoView.fxml", parent);
    }

    @FXML
    public void onBtDesmatricularAlunoAction(ActionEvent event) {
        Stage parent = Utils.currentStage(event);
        createFormDesmatricularAluno(new Aluno(), new AlunoContato(), "/br/com/view/secretaria/DesmatricularAlunoView.fxml", parent);
    }

    @FXML
    public void onBtBuscarAlunoAction(ActionEvent event) {
        try {
            String query = generateQuerySearch();
            updateTableViewSeach(query);
        }
        catch (DbException e) {
            Alerts.showAlert("Erro ao acessar os dados", null, "Preencha os campos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtListarTodosAlunosAction(ActionEvent event) {
        try {
            updateTableView();
        }
        catch (DbException e) {
            Alerts.showAlert("Erro ao acessar os dados", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<AlunoDTO> obsList;

    public void setServices(AlunoService alunoService, AlunoContatoService alunoContatoService) {
        this.alunoService = alunoService;
        this.alunoContatoService = alunoContatoService;
    }

    private String generateQuerySearch() {
        String query = "";
        int counter = 0;

        if (!txtBuscaPorNome.getText().trim().equals("")) {
            if (counter == 0) query += "p.Nome LIKE '" + txtBuscaPorNome.getText() + "%'";
            else query += " OR p.Nome LIKE '" + txtBuscaPorNome.getText() + "%'";
            counter++;
        }

        if (!txtBuscaPorRa.getText().trim().equals("")) {
            if (counter == 0) query += "am.RA LIKE '" + txtBuscaPorRa.getText() + "%'";
            else query += " OR am.RA LIKE '" + txtBuscaPorRa.getText() + "%'";
            counter++;
        }

        return query;
    }

    private void updateTableViewSeach(String query) {
        if (alunoService == null) {
            throw new IllegalStateException("Aluno service was null");
        }

        List<AlunoDTO> list = new ArrayList<>();

        for (Aluno aluno : alunoService.search(query)) {
            list.add(new AlunoDTO(aluno, aluno.getAluno_matricula()));
        }

        obsList = FXCollections.observableList(list);
        tableViewAluno.setItems(obsList);
    }

    private void createDialogForm(Aluno objAluno, Pessoa objPessoa, AlunoMatricula objAlunoMatricula, AlunoContato objAlunoContato, String absoluteName, Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            MatricularAlunoViewController controller = loader.getController();
            controller.setEntities(objAluno, objPessoa, objAlunoMatricula, objAlunoContato);
            controller.setServices(new AlunoService(), new PessoaService(), new AlunoMatriculaService(), new AlunoContatoService(), new TurmaService());
//            controller.setDataChangedListener(this);
//            controller.setMonitorsRecentNew(this);
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

    private void createFormDesmatricularAluno(Aluno objAluno, AlunoContato objAlunoContato, String absoluteName, Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DesmatricularAlunoViewController controller = loader.getController();
            controller.setServices(new AlunoService(), new PessoaService(), new AlunoMatriculaService(), new AlunoContatoService());
//            controller.subscribeDataChangedListener(this);

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

    private void exibirDetalhesAluno(AlunoDTO obj, ActionEvent event) {
        Aluno aluno = new Aluno(obj.getId(), obj.getPessoa(), obj.getAlunoMatricula());

        AlunoContato contato = null;

        List<AlunoContato> list = alunoContatoService.findAll();
        for (AlunoContato alunoContato : list) {
            if (alunoContato.getAluno().getId().equals(obj.getId())) {
                contato = alunoContato;
            }
        }

        createDialogForm(aluno, aluno.getPessoa(), aluno.getAluno_matricula(), contato, "/br/com/view/secretaria/MatricularAlunoView.fxml", Utils.currentStage(event));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnRG.setCellValueFactory(new PropertyValueFactory<>("rg"));
        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("serie"));
        tableColumnTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        tableColumnRA.setCellValueFactory(new PropertyValueFactory<>("RA"));
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
}
