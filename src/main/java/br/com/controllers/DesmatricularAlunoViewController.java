package br.com.controllers;

import br.com.exceptions.DbException;
import br.com.exceptions.ValidationException;
import br.com.listeners.DataChangedListener;
import br.com.listeners.DataChangedListenerAlunoDTO;
import br.com.model.dto.AlunoDTO;
import br.com.model.entities.Aluno;
import br.com.model.entities.AlunoContato;
import br.com.model.entities.Turma;
import br.com.model.services.AlunoContatoService;
import br.com.model.services.AlunoMatriculaService;
import br.com.model.services.AlunoService;
import br.com.model.services.PessoaService;
import br.com.util.Alerts;
import br.com.util.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DesmatricularAlunoViewController implements Initializable, DataChangedListenerAlunoDTO {

    private AlunoDTO entityDto;

    private AlunoService alunoService;

    private PessoaService pessoaService;

    private AlunoMatriculaService alunoMatriculaService;

    private AlunoContatoService alunoContatoService;

    @FXML
    private TextField txtNomePesquisa;

    @FXML
    private TextField txtRaPesquisa;

    @FXML
    private TextField txtConfRaPesquisa;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtRA;

    @FXML
    private TextField txtRg;

    @FXML
    private TextField txtSexo;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSerie;

    @FXML
    private TextField txtTurma;

    @FXML
    private TextField txtEmailUsuario;

    @FXML
    private TextField txtSenhaUsuario;

    @FXML
    private Button btBuscar;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;

    @FXML
    public void onBtBuscarAction(ActionEvent event) {
        if (alunoService == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            String query = generateQuerySearch();
            List<Aluno> list = alunoService.search(query);

            Stage parent = Utils.currentStage(event);
            loadViewSelectStudent("/br/com/view/SelecionarAlunoDesmatricularView.fxml", parent, list);
        }
        catch (ValidationException e) {
            Alerts.showAlert("Erro ao buscar aluno", null, e.getErrors().get("ra"), Alert.AlertType.ERROR);
        }
        catch (DbException e) {
            Alerts.showAlert("Erro ao acessar os dados", null, "Preencha os campos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtConfirmarAction(ActionEvent event) {
        if (alunoService == null || pessoaService == null || alunoMatriculaService == null) {
            throw new IllegalStateException("Services was null");
        }
        try {
            for (Aluno aluno : alunoService.findAll()) {
                System.out.println(entityDto);
                if (aluno.getId().equals(entityDto.getId())) {
                    int ID_PESSOA = aluno.getPessoa().getId();
                    int ID_ALUNO = aluno.getId();
                    int ID_ALUNO_MATRICULA = aluno.getAluno_matricula().getId();

                    alunoContatoService.deleteById(ID_ALUNO);
                    alunoService.deleteById(ID_PESSOA);
                    pessoaService.deleteById(ID_PESSOA);
                    alunoMatriculaService.deleteById(ID_ALUNO_MATRICULA);

                    notifyDataChangedListener();

                    Alerts.showAlert("Operacão realizada com sucesso", null, "Aluno desmatrículado", Alert.AlertType.INFORMATION);
                    Utils.currentStage(event).close();
                }
            }
        }
        catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro ao acessar os dados", null, "Não foi possível desmatricular", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtCancelarAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private List<DataChangedListener> dataChangedListeners = new ArrayList<>();

    public void setServices(AlunoService alunoService, PessoaService pessoaService,
                            AlunoMatriculaService alunoMatriculaService, AlunoContatoService alunoContatoService
    ) {
        this.alunoService = alunoService;
        this.pessoaService = pessoaService;
        this.alunoMatriculaService = alunoMatriculaService;
        this.alunoContatoService = alunoContatoService;
    }

    public void subscribeDataChangedListener(DataChangedListener listener) {
        dataChangedListeners.add(listener);
    }

    private String generateQuerySearch() {
        String query = "";
        int counter = 0;

        ValidationException exception = new ValidationException("Validation exception");

        if (!txtNomePesquisa.getText().trim().equals("")) {
            if (counter == 0) query += "p.Nome LIKE '" + txtNomePesquisa.getText() + "%'";
            else query += " OR p.Nome LIKE '" + txtNomePesquisa.getText() + "%'";
            counter++;
        }

        if (!txtRaPesquisa.getText().equals(txtConfRaPesquisa.getText())) {
            exception.setErrors("ra", "Os RA não se coincidem");
        }

        if (!txtRaPesquisa.getText().trim().equals("") && !txtConfRaPesquisa.getText().trim().equals("")) {
            if (counter == 0) {
                query += "am.RA LIKE '" + txtRaPesquisa.getText() + "%'";
            }
            else query += " OR am.RA LIKE '" + txtRaPesquisa.getText() + "%'";
            counter++;
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return query;
    }

    private void updateDataAluno(AlunoDTO objDto) {
        if (objDto == null) {
            throw new IllegalStateException("Aluno was null");
        }

        txtNome.setText(objDto.getPessoa().getNome());
        txtRA.setText(objDto.getRA());
        txtRg.setText(objDto.getPessoa().getRg());
        txtSexo.setText(objDto.getPessoa().getSexo());
        txtEmail.setText(objDto.getPessoa().getEmail());
        txtSerie.setText(objDto.getSerie());
        txtTurma.setText(objDto.getTurma());
    }

    private void loadViewSelectStudent(String absoluteName, Stage parent, List<Aluno> list) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SelecionarAlunoDesmatricularViewController controller = loader.getController();
            controller.updateTableView(list);
            controller.subscribeDataChangedListener(this);

            Stage stage = new Stage();
            stage.setTitle("Selecione um aluno");
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.initOwner(parent);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Erro ao carregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangedListener() {
        for (DataChangedListener listener : dataChangedListeners) {
            listener.onDataChangedListener();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        //
    }

    @Override
    public void onDataChangedListenerAlunoDTO(AlunoDTO objDto) {
        updateDataAluno(objDto);
        this.entityDto = objDto;
    }
}
