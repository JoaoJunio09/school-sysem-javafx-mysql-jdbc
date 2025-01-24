package br.com.controllers;

import br.com.exceptions.DbException;
import br.com.exceptions.ValidationException;
import br.com.model.entities.Usuario;
import br.com.model.services.AlunoContatoService;
import br.com.model.services.AlunoService;
import br.com.model.services.LoginService;
import br.com.model.services.TurmaService;
import br.com.schoolsystem.Main;
import br.com.util.Alerts;
import br.com.util.Constraints;
import br.com.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class LoginViewController implements Initializable {

    private LoginService service;

    private Usuario entity;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pswPassword;

    @FXML
    private ComboBox<String> comboBoxTipoUsuario;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorSenha;

    @FXML
    private Label labelErrorComboBox;

    @FXML
    private Button btEntrar;

    @FXML
    private Button btLimpar;

    private AnchorPane anchorPane;

    private ObservableList<String> obsListTipoUsuario;

    public void setService(LoginService service) {
        this.service = service;
    }

    public void setEntity(Usuario entity) {
        this.entity = entity;
    }

    @FXML
    public void onBtEntrarAction(ActionEvent event) {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        int userCorrect = 0;
        try {
            entity = getFormData();
            List<Usuario> listUsers = service.findAll();

            for (Usuario user : listUsers) {
                if (user.getEmail().equals(entity.getEmail()) &&
                    user.getSenha().equals(entity.getSenha()) &&
                    user.getTipoUsuario().equals(entity.getTipoUsuario())
                ) {
                    userCorrect = 2;
                }
                if (!user.getTipoUsuario().equals(entity.getTipoUsuario())) {
                    userCorrect = 1;
                }
            }
        }
        catch (ValidationException e) {
            setMessageError(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Db Exception", null, e.getMessage(), Alert.AlertType.ERROR);
        }

        if (userCorrect == 0) {
            Alerts.showAlert("Erro ao fazer login", null, "E-mail ou senha incorretos", Alert.AlertType.ERROR);
        }
        if (userCorrect == 1) {
            Alerts.showAlert("Erro ao fazer login", null, "Tipo de usuário incorreto", Alert.AlertType.ERROR);
        }
        if (userCorrect == 2) {
            loadViewMain("/br/com/view/MainSecretariaView.fxml", (MainSecretariaViewController controller) -> {
                controller.setEntityUser(entity);
                controller.setServices(new AlunoService(), new AlunoContatoService(), new TurmaService());
//                controller.updateNumberAlunos();
                controller.updateDataEntityUser();
                controller.updateTableView();
            }, event);
        }
    }

    @FXML
    public void onBtLimparAction() {
        txtEmail.setText("");
        pswPassword.setText("");
    }

    private Usuario getFormData() {
        Usuario obj = new Usuario();

        ValidationException exception = new ValidationException("Validation exception");

        obj.setId(null);

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
            exception.setErrors("email", "Informe um e-mail");
        }
        obj.setEmail(txtEmail.getText());

        if (pswPassword.getText() == null || pswPassword.getText().trim().equals("")) {
            exception.setErrors("senha", "Informe uma senha");
        }
        obj.setSenha(pswPassword.getText());

        if (comboBoxTipoUsuario.getValue() == null) {
            exception.setErrors("tipoUsuario", "Informe qual o usuário");
        }
        else {
            if (comboBoxTipoUsuario.getValue().equals("Secretaria")) {
                obj.setTipoUsuario(1);
            }
            else if (comboBoxTipoUsuario.getValue().equals("Aluno")) {
                obj.setTipoUsuario(2);
            }
            else if (comboBoxTipoUsuario.getValue().equals("Professor")) {
                obj.setTipoUsuario(3);
            }
            else {
                obj.setTipoUsuario(4);
            }
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    public void updateFormDataLogin() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtEmail.setText(entity.getEmail());
        pswPassword.setText(entity.getSenha());
        if (entity.getTipoUsuario() == null) {
            comboBoxTipoUsuario.getSelectionModel().selectFirst();
        }
        else {
            comboBoxTipoUsuario.setValue(identityUserTypeNumber(entity.getTipoUsuario()));
        }
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

    private <T> void loadViewMain(String absoluteName, Consumer<T> initializing, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            AnchorPane anchorPane = loader.load();

            Scene mainScene = new Scene(anchorPane);
            Main.setMainScene(mainScene);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setTitle("S& Application");
            mainStage.show();

            Utils.currentStage(event).close();

            T controller = loader.getController();
            initializing.accept(controller);
        }
        catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Erro ao carregar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setMessageError(Map<String, String> errors) {
        Set<String> field = errors.keySet();

        labelErrorEmail.setText((field.contains("email") ? errors.get("email") : ""));
        labelErrorSenha.setText((field.contains("senha") ? errors.get("senha") : ""));
        labelErrorComboBox.setText((field.contains("tipoUsuario") ? errors.get("tipoUsuario") : ""));
    }

    public void loadAssociateTipoUsuario() {
        List<String> list = Arrays.asList("Secretaria", "Aluno", "Professor", "Gestão Escolar");

        if (list.isEmpty()) {
            throw new IllegalStateException("List Tipo Usuário was empty");
        }
        obsListTipoUsuario = FXCollections.observableArrayList(list);
        comboBoxTipoUsuario.setItems(obsListTipoUsuario);
    }

    private void initializeComboBoxTipoUsuario() {
        Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.toString());
            }
        };
        comboBoxTipoUsuario.setCellFactory(factory);
        comboBoxTipoUsuario.setButtonCell(factory.call(null));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldMaxLength(pswPassword, 12);
        
        initializeComboBoxTipoUsuario();
    }
}
