package br.com.controllers;

import br.com.listeners.DataChangedListener;
import br.com.listeners.DataChangedListenerAlunoDTO;
import br.com.model.dto.AlunoDTO;
import br.com.model.entities.Aluno;
import br.com.model.services.AlunoService;
import br.com.util.Alerts;
import br.com.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelecionarAlunoDesmatricularViewController implements Initializable {

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

    private ObservableList<AlunoDTO> obsList;

    private List<DataChangedListenerAlunoDTO> dataChangedListeners = new ArrayList<>();

    public void subscribeDataChangedListener(DataChangedListenerAlunoDTO listener) {
        dataChangedListeners.add(listener);
    }

    public void updateTableView(List<Aluno> list) {
        List<AlunoDTO> listDto = new ArrayList<>();

        for (Aluno aluno : list) {
            listDto.add(new AlunoDTO(aluno, aluno.getAluno_matricula()));
        }

        obsList = FXCollections.observableArrayList(listDto);
        tableViewAluno.setItems(obsList);

        initSelecionarButtons();
    }

    private void notifyDataChangedListener(AlunoDTO objDto) {
        for (DataChangedListenerAlunoDTO listener : dataChangedListeners) {
            listener.onDataChangedListenerAlunoDTO(objDto);
        }
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

    private void selectStudent(AlunoDTO alunoDto, ActionEvent event) {
        if (alunoDto.getId() == null) {
            Alerts.showAlert("Erro ao selecionar o Aluno", null, "Aluno não existe", Alert.AlertType.ERROR);
        }

        notifyDataChangedListener(alunoDto);
        Utils.currentStage(event).close();
    }

    private void initSelecionarButtons() {
        tableColumnDETALHES.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDETALHES.setCellFactory(param -> new TableCell<AlunoDTO, AlunoDTO>() {
            private final Button button = new Button("Selecionar");

            @Override
            protected void updateItem(AlunoDTO obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> selectStudent(obj, event));
            }
        });
    }
}
