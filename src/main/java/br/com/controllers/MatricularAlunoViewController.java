package br.com.controllers;

import br.com.exceptions.DbException;
import br.com.exceptions.UserAlreadyExists;
import br.com.exceptions.ValidationException;
import br.com.listeners.DataChangedListener;
import br.com.listeners.MonitorsRecentNew;
import br.com.model.entities.*;
import br.com.model.services.*;
import br.com.util.Alerts;
import br.com.util.Constraints;
import br.com.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class MatricularAlunoViewController implements Initializable {

    private Aluno entityAluno;

    private Pessoa entityPessoa;

    private AlunoMatricula entityAlunoMatricula;

    private AlunoContato entityAlunoContato;

    private AlunoService alunoService;

    private PessoaService pessoaService;

    private AlunoMatriculaService alunoMatriculaService;

    private AlunoContatoService alunoContatoService;

    private TurmaService turmaService;

    @FXML
    private TextField txtIdPessoa;

    @FXML
    private TextField txtNomePessoa;

    @FXML
    private TextField txtEnderecoResPessoa;

    @FXML
    private TextField txtComplementoPessoa;

    @FXML
    private TextField txtNumeroPessoa;

    @FXML
    private TextField txtBairroPessoa;

    @FXML
    private TextField txtCepPessoa;

    @FXML
    private TextField txtEmailPessoa;

    @FXML
    private DatePicker dpDataNascimentoPessoa;

    @FXML
    private TextField txtSexoPessoa;

    @FXML
    private TextField txtCpfPessoa;

    @FXML
    private TextField txtRgPessoa;

    @FXML
    private TextField txtNaturalidadePessoa;

    @FXML
    private TextField txtNacionalidadePessoa;

    @FXML
    private TextField txtIdMatricula;

    @FXML
    private ComboBox<String> comboBoxCorRaca;

    @FXML
    private ComboBox<String> comboBoxDeficiencia;

    @FXML
    private ComboBox<String> comboBoxTipoSanguineo;

    @FXML
    private TextArea txtAreaNecessidadesEspeciais;

    @FXML
    private DatePicker dpDataMatricula;

    @FXML
    private TextField txtRA;

    @FXML
    private ComboBox<Turma> comboBoxTurma;

    @FXML
    private TextField txtIdContato;

    @FXML
    private TextArea txtAreaDescricao;

    @FXML
    private TextArea txtAreaContato;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btLimparCampos;

    @FXML
    public void btConfirmarAction(ActionEvent event) {
        if (entityAluno == null|| entityAlunoContato == null) {
            throw new IllegalStateException("Entities was null");
        }
        if (alunoService == null) {
            throw new IllegalStateException("Aluno service was null");
        }
        try {
            entityPessoa = getFormDataPessoa();
            pessoaService.saveOrUpdate(entityPessoa);
            entityAlunoMatricula = getFormDataMatricula();
            alunoMatriculaService.saveOrUpdate(entityAlunoMatricula);
            entityAluno = getFormDataAluno();
            alunoService.saveOrUpdate(entityAluno);
            entityAlunoContato = getFormDataContato();
            alunoContatoService.saveOrUpdate(entityAlunoContato);
            notifyDataChangedListener();

            notifyMonitorRecentNew("Aluno " + entityPessoa.getNome() + " matriculado/alterado, Série: " + entityAlunoMatricula.getTurma().getSerie() + " " + entityAlunoMatricula.getTurma().getTurma());

            Alerts.showAlert("Concluido", null, "Aluno salvo com sucesso", Alert.AlertType.INFORMATION);
            Utils.currentStage(event).close();
        }
        catch (UserAlreadyExists e) {
            Alerts.showAlert("Erro ao salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (ValidationException e) {
            setMessageErrors(e.getErrors());
        }
        catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Erros críticos ao salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtCancelarAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @FXML
    public void onBtLimparCamposAction() {
        txtIdPessoa.setText("");
        txtNomePessoa.setText("");
        txtEnderecoResPessoa.setText("");
        txtComplementoPessoa.setText("");
        txtNumeroPessoa.setText("");
        txtBairroPessoa.setText("");
        txtCepPessoa.setText("");
        txtEmailPessoa.setText("");
        dpDataNascimentoPessoa.setValue(null);
        txtSexoPessoa.setText("");
        txtCpfPessoa.setText("");
        txtRgPessoa.setText("");
        txtNaturalidadePessoa.setText("");
        txtNacionalidadePessoa.setText("");
        txtIdMatricula.setText("");
        comboBoxCorRaca.setValue("");
        comboBoxDeficiencia.setValue("");
        comboBoxTipoSanguineo.setValue("");
        txtAreaNecessidadesEspeciais.setText("");
        dpDataMatricula.setValue(null);
        txtRA.setText("");
        comboBoxTurma.setValue(null);
        txtIdContato.setText("");
        txtAreaDescricao.setText("");
        txtAreaContato.setText("");
    }

    private ObservableList<Turma> obsListTurma;

    private ObservableList<String> obsListCorRaca;

    private ObservableList<String> obsListDeficiencia;

    private ObservableList<String> obsListTipoSanguineo;

    private List<DataChangedListener> dataChangedListeners = new ArrayList<>();

    private List<MonitorsRecentNew> monitorsRecentNews = new ArrayList<>();

    public void setEntities(Aluno alunoEntity, Pessoa pessoaEntity, AlunoMatricula alunoMatriculaEntity,
                            AlunoContato alunoContatoEntity
    ) {
        this.entityAluno = alunoEntity;
        this.entityPessoa = pessoaEntity;
        this.entityAlunoMatricula = alunoMatriculaEntity;
        this.entityAlunoContato = alunoContatoEntity;
    }

    public void setServices(AlunoService alunoService, PessoaService pessoaService, AlunoMatriculaService alunoMatriculaService,
                            AlunoContatoService alunoContatoService, TurmaService turmaService
    ) {
        this.alunoService = alunoService;
        this.pessoaService = pessoaService;
        this.alunoMatriculaService = alunoMatriculaService;
        this.alunoContatoService = alunoContatoService;
        this.turmaService = turmaService;
    }

    public void setDataChangedListener(DataChangedListener dataChangedListener) {
        this.dataChangedListeners.add(dataChangedListener);
    }

    public void setMonitorsRecentNew(MonitorsRecentNew monitorsRecentNew) {
        this.monitorsRecentNews.add(monitorsRecentNew);
    }

    private Aluno getFormDataAluno() {
        Aluno obj = new Aluno();

        obj.setId(null);

        obj.setPessoa(entityPessoa);
        obj.setAluno_matricula(entityAlunoMatricula);

        return obj;
    }

    private Pessoa getFormDataPessoa() {
        Pessoa obj = new Pessoa();

        ValidationException exception = new ValidationException("Validation exception");

        obj.setId(Utils.tryParseToInt(txtIdPessoa.getText()));

        if (
                txtNomePessoa.getText() == null || txtNomePessoa.getText().trim().equals("") ||
                txtEnderecoResPessoa.getText() == null || txtEnderecoResPessoa.getText().trim().equals("") ||
                txtComplementoPessoa.getText() == null || txtComplementoPessoa.getText().trim().equals("") ||
                txtNumeroPessoa.getText() == null || txtNumeroPessoa.getText().trim().equals("") ||
                txtBairroPessoa.getText() == null || txtBairroPessoa.getText().trim().equals("") ||
                txtCepPessoa.getText() == null || txtCepPessoa.getText().trim().equals("") ||
                txtEmailPessoa.getText() == null || txtEmailPessoa.getText().trim().equals("") ||
                dpDataNascimentoPessoa.getValue() == null ||
                txtSexoPessoa.getText() == null || txtSexoPessoa.getText().trim().equals("") ||
                txtCpfPessoa.getText() == null || txtCpfPessoa.getText().trim().equals("") ||
                txtRgPessoa.getText() == null || txtRgPessoa.getText().trim().equals("") ||
                txtNaturalidadePessoa.getText() == null || txtNaturalidadePessoa.getText().trim().equals("") ||
                txtNacionalidadePessoa.getText() == null || txtNacionalidadePessoa.getText().trim().equals("")
        ) {
            exception.setErrors("errors", "Preencha todos os campos");
        }
        else {
            Instant instant = Instant.from(dpDataNascimentoPessoa.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setData_nascimento(Date.from(instant));
        }

        obj.setNome(txtNomePessoa.getText());
        obj.setEndereco_res(txtEnderecoResPessoa.getText());
        obj.setComplemento(txtComplementoPessoa.getText());
        obj.setNumero(txtNumeroPessoa.getText());
        obj.setBairro(txtBairroPessoa.getText());
        obj.setCep(txtCepPessoa.getText());
        obj.setEmail(txtEmailPessoa.getText());
        obj.setSexo(txtSexoPessoa.getText());
        obj.setCpf(txtCpfPessoa.getText());
        obj.setRg(txtRgPessoa.getText());
        obj.setNaturalidade(txtNaturalidadePessoa.getText());
        obj.setNacionalidade(txtNacionalidadePessoa.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        for (Pessoa pessoa : pessoaService.findAll()) {
            if (pessoa.getCpf().equals(obj.getCpf()) || pessoa.getRg().equals(obj.getRg())) {
                throw new UserAlreadyExists("Essa pessoa já existe");
            }
        }

        return obj;
    }

    private AlunoMatricula getFormDataMatricula() {
        AlunoMatricula obj = new AlunoMatricula();

        ValidationException exception = new ValidationException("Validation exception");

        obj.setId(Utils.tryParseToInt(txtIdMatricula.getText()));

        if (
                comboBoxCorRaca.getValue() == null ||
                comboBoxDeficiencia.getValue() == null ||
                comboBoxTipoSanguineo.getValue() == null ||
                txtAreaNecessidadesEspeciais.getText() == null || txtAreaNecessidadesEspeciais.getText().trim().equals("") ||
                dpDataMatricula.getValue() == null ||
                comboBoxTurma.getValue() == null
        ) {
            exception.setErrors("errors", "Preencha todos os campos");
        }
        else {
            Instant instant = Instant.from(dpDataMatricula.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setData_matricula(Date.from(instant));
        }

        obj.setCor_raca(comboBoxCorRaca.getValue());
        obj.setDeficiencia(comboBoxDeficiencia.getValue());
        obj.setTipo_sanguineo(comboBoxTipoSanguineo.getValue());
        obj.setNecessidades_especiais(txtAreaNecessidadesEspeciais.getText());
        obj.setRA("example123RA");
        obj.setTurma(comboBoxTurma.getValue());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    private AlunoContato getFormDataContato() {
        AlunoContato obj = new AlunoContato();

        ValidationException exception = new ValidationException("Validation exception");

        obj.setId(Utils.tryParseToInt(txtIdContato.getText()));

        if (
                txtAreaDescricao.getText() == null || txtAreaDescricao.getText().trim().equals("") ||
                txtAreaContato.getText() == null || txtAreaContato.getText().trim().equals("")
        ) {
            exception.setErrors("errors", "Preencha todos os campos");
        }

        obj.setDescricao(txtAreaDescricao.getText());
        obj.setContato(txtAreaContato.getText());
        obj.setAluno(entityAluno);

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    public void updateFormData() {
        if (entityAluno == null || entityPessoa == null || entityAlunoMatricula == null || entityAlunoContato == null) {
            throw new IllegalStateException("Entities was null");
        }

        boolean userExiting = false;
        if (entityAluno.getId() != null) userExiting = true;
        editableFieldsForm(userExiting);

        txtIdPessoa.setText(String.valueOf(entityPessoa.getId()));
        txtNomePessoa.setText(entityPessoa.getNome());
        txtEnderecoResPessoa.setText(entityPessoa.getEndereco_res());
        txtComplementoPessoa.setText(entityPessoa.getComplemento());
        txtNumeroPessoa.setText(entityPessoa.getNumero());
        txtBairroPessoa.setText(entityPessoa.getBairro());
        txtCepPessoa.setText(entityPessoa.getCep());
        txtEmailPessoa.setText(entityPessoa.getEmail());
        if (entityPessoa.getData_nascimento() != null) {
            dpDataNascimentoPessoa.setValue(LocalDate.ofInstant(entityPessoa.getData_nascimento().toInstant(), ZoneId.systemDefault()));
        }
        txtSexoPessoa.setText(entityPessoa.getSexo());
        txtCpfPessoa.setText(entityPessoa.getCpf());
        txtRgPessoa.setText(entityPessoa.getRg());
        txtNaturalidadePessoa.setText(entityPessoa.getNaturalidade());
        txtNacionalidadePessoa.setText(entityPessoa.getNacionalidade());
        txtIdMatricula.setText(String.valueOf(entityAluno.getId()));
        if (entityAlunoMatricula.getCor_raca() == null) {
            comboBoxCorRaca.getSelectionModel().selectFirst();
        }
        else {
            comboBoxCorRaca.setValue(entityAlunoMatricula.getCor_raca());
        }
        if (entityAlunoMatricula.getDeficiencia() == null) {
            comboBoxDeficiencia.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDeficiencia.setValue(entityAlunoMatricula.getDeficiencia());
        }
        if (entityAlunoMatricula.getTipo_sanguineo() == null) {
            comboBoxTipoSanguineo.getSelectionModel().selectFirst();
        }
        else {
            comboBoxTipoSanguineo.setValue(entityAlunoMatricula.getTipo_sanguineo());
        }
        txtAreaNecessidadesEspeciais.setText(entityAlunoMatricula.getNecessidades_especiais());
        if (entityAlunoMatricula.getData_matricula() != null) {
            dpDataMatricula.setValue(LocalDate.ofInstant(entityAlunoMatricula.getData_matricula().toInstant(), ZoneId.systemDefault()));
        }
        txtRA.setText(entityAlunoMatricula.getRA());
        if (entityAlunoMatricula.getTurma() == null) {
            comboBoxTurma.getSelectionModel().selectFirst();
        }
        else {
            comboBoxTurma.setValue(entityAlunoMatricula.getTurma());
        }

        txtIdContato.setText(String.valueOf(entityAlunoContato.getId()));
        txtAreaDescricao.setText(entityAlunoContato.getDescricao());
        txtAreaContato.setText(entityAlunoContato.getContato());
    }

    private void editableFieldsForm(boolean userExiting) {
        if (userExiting) {
            btLimparCampos.setDisable(Boolean.TRUE);
            btConfirmar.setDisable(Boolean.TRUE);

            txtNomePessoa.setEditable(false);
            txtEnderecoResPessoa.setEditable(false);
            txtComplementoPessoa.setEditable(false);
            txtNumeroPessoa.setEditable(false);
            txtBairroPessoa.setEditable(false);
            txtCepPessoa.setEditable(false);
            txtEmailPessoa.setEditable(false);
            dpDataNascimentoPessoa.setEditable(false);
            txtSexoPessoa.setEditable(false);
            txtCpfPessoa.setEditable(false);
            txtRgPessoa.setEditable(false);
            txtNaturalidadePessoa.setEditable(false);
            txtNacionalidadePessoa.setEditable(false);
            comboBoxCorRaca.setEditable(false);
            comboBoxDeficiencia.setEditable(false);
            comboBoxTipoSanguineo.setEditable(false);
            txtAreaNecessidadesEspeciais.setEditable(false);
            dpDataMatricula.setEditable(false);
            comboBoxTurma.setEditable(false);
            txtAreaDescricao.setEditable(false);
            txtAreaContato.setEditable(false);
        }
    }

    private void setMessageErrors(Map<String, String> errors) {
        Set<String> field = errors.keySet();

        if (field.contains("errors")) {
            Alerts.showAlert("Erro ao salvar", null, errors.get("errors"), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangedListener() {
        for (DataChangedListener listener : dataChangedListeners) {
            listener.onDataChangedListener();
        }
    }

    private void notifyMonitorRecentNew(String message) {
        for (MonitorsRecentNew recents : monitorsRecentNews) {
            recents.onMonitorsRecentNew(message);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtIdPessoa);
        Constraints.setTextFieldInteger(txtIdMatricula);
        Constraints.setTextFieldInteger(txtIdContato);

        Utils.formatDatePicker(dpDataNascimentoPessoa, "dd/MM/yyyy");
        Utils.formatDatePicker(dpDataMatricula, "dd/MM/yyyy");

        initializeComboBoxCorRaca();
        initializeComboBoxDeficiencia();
        initializeComboBoxTipoSanguineo();
        initializeComboBoxTurma();
    }

    public void loadAssociatedAllComboBox() {
        loadAssociatedCorRaca();
        loadAssociatedDeficiencia();
        loadAssociatedTipoSanguineo();
        loadAssociatedTurma();
    }

    private void loadAssociatedTurma() {
        if (turmaService == null) {
            throw new IllegalStateException("Turma Service was null");
        }

        List<Turma> list = turmaService.findAll();
        obsListTurma = FXCollections.observableArrayList(list);
        comboBoxTurma.setItems(obsListTurma);
    }

    private void loadAssociatedCorRaca() {
        List<String> list = Arrays.asList("Preto", "Pardo", "Branco", "Indígena", "Amarelo");
        obsListCorRaca = FXCollections.observableArrayList(list);
        comboBoxCorRaca.setItems(obsListCorRaca);
    }

    private void loadAssociatedDeficiencia() {
        List<String> list = Arrays.asList("Sim", "Não");
        obsListDeficiencia = FXCollections.observableArrayList(list);
        comboBoxDeficiencia.setItems(obsListDeficiencia);
    }

    private void loadAssociatedTipoSanguineo() {
        List<String> list = Arrays.asList("A+", "B+", "AB+", "O+", "A", "B", "AB", "O", "Não informado");
        obsListTipoSanguineo = FXCollections.observableArrayList(list);
        comboBoxTipoSanguineo.setItems(obsListTipoSanguineo);
    }

    private void initializeComboBoxTurma() {
        Callback<ListView<Turma>, ListCell<Turma>> factory = lv -> new ListCell<Turma>() {
            @Override
            protected void updateItem(Turma item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.toString());
            }
        };
        comboBoxTurma.setCellFactory(factory);
        comboBoxTurma.setButtonCell(factory.call(null));
    }

    private void initializeComboBoxCorRaca() {
        Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        };
        comboBoxCorRaca.setCellFactory(factory);
        comboBoxCorRaca.setButtonCell(factory.call(null));
    }

    private void initializeComboBoxDeficiencia() {
        Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        };
        comboBoxDeficiencia.setCellFactory(factory);
        comboBoxDeficiencia.setButtonCell(factory.call(null));
    }

    private void initializeComboBoxTipoSanguineo() {
        Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        };
        comboBoxTipoSanguineo.setCellFactory(factory);
        comboBoxTipoSanguineo.setButtonCell(factory.call(null));
    }
}
