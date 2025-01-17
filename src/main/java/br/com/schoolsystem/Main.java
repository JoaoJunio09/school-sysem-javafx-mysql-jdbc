package br.com.schoolsystem;

import br.com.controllers.LoginViewController;
import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.*;
import br.com.model.services.LoginService;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class Main extends javafx.application.Application {

    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/br/com/view/LoginView.fxml"));
        Pane paneLogin = fxmlLoader.load();

        LoginViewController controller = fxmlLoader.getController();
        controller.setEntity(new Usuario());
        controller.setService(new LoginService());
        controller.loadAssociateTipoUsuario();
        controller.updateFormDataLogin();

        Scene loginScene = new Scene(paneLogin);
        stage.setTitle("School System - application login");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();

        CRUD<Pessoa> pessoaDao = DaoFactory.createPessoaDaoJDBC();
        CRUD<Turma> turmaDao = DaoFactory.createTurmaDaoJDBC();
        CRUD<AlunoMatricula> alunoMDao = DaoFactory.createAlunoMatriculaDaoJDBC();
        CRUD<Aluno> alunoDao = DaoFactory.createAlunoDaoJDBC();
        CRUD< AlunoContato> alunoContatoDao = DaoFactory.createAlunoContatoDaoJDBC();
        CRUD<ProfessorMatricula> profMatriculaDao = DaoFactory.createProfessorMatriculaDaoJDBC();
        CRUD<Professor> profDao = DaoFactory.createProfessorDaoJDBC();
        CRUD<ProfessorContato> profContatoDao = DaoFactory.createProfessorContatoDaoJDBC();
        CRUD<Disciplina> disciplinaDao = DaoFactory.createDisciplinaDaoJDBC();
        CRUD<ProfessorDisciplina> profDisciplinaDao = DaoFactory.createProfessorDisciplinaDaoJDBC();
        CRUD<ProfessorTurma> profTurmaDao = DaoFactory.createProfessorTurmaDaoJDBC();
        CRUD<Funcionario> funcionarioDao = DaoFactory.createFuncionarioDaoJDBC();
        CRUD<FuncionarioContato> funcionarioContatoDao = DaoFactory.createFuncionarioContatoDaoJDBC();

        Turma turma = turmaDao.findById(4);
        Professor professor = profDao.findById(2);
        Disciplina disciplina = disciplinaDao.findById(4);

//        List<ProfessorDisciplina> list = profDisciplinaDao.findAll()
//                .stream()
//                .filter(x -> x.getProfessor().getId().equals(professor.getId()))
//                .collect(Collectors.toList());

    }

    public static Scene getMainScene() { return mainScene; }

    public static void setMainScene(Scene scene) {
        mainScene = scene;
    }

    public static void main(String[] args) {
        launch();
    }
}