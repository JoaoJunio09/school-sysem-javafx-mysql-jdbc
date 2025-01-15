package br.com.schoolsystem;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.dao.impl.AlunoMatriculaDaoJDBC;
import br.com.model.entities.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Application extends javafx.application.Application {

    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/br/com/view/MainView.fxml"));
//        AnchorPane anchorPane = fxmlLoader.load();
//
//        mainScene = new Scene(anchorPane);
//        stage.setTitle("School System - application");
//        stage.setScene(mainScene);
//        stage.show();

        CRUD<Pessoa> pessoaDao = DaoFactory.createPessoaDaoJDBC();
        CRUD<Turma> turmaDao = DaoFactory.createTurmaDaoJDBC();
        CRUD<AlunoMatricula> alunoMDao = DaoFactory.createAlunoMatriculaDaoJDBC();
        CRUD<Aluno> alunoDao = DaoFactory.createAlunoDaoJDBC();
        CRUD< AlunoContato> alunoContatoDao = DaoFactory.createAlunoContatoDaoJDBC();
        CRUD<ProfessorMatricula> profMatriculaDao = DaoFactory.createProfessorMatriculaDaoJDBC();
        CRUD<Professor> profDao = DaoFactory.createProfessorDaoJDBC();
        CRUD<ProfessorContato> profContatoDao = DaoFactory.createProfessorContatoDaoJDBC();

    }

    public static Scene getMainScene() { return mainScene; }

    public static void main(String[] args) {
        launch();
    }
}