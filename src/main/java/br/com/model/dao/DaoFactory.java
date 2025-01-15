package br.com.model.dao;

import br.com.db.DB;
import br.com.model.dao.impl.*;
import br.com.model.dao.impl.ProfessorDisciplinaDaoJDBC;
import br.com.model.entities.*;

public class DaoFactory {

    public static CRUD<Pessoa> createPessoaDaoJDBC() {
        return new PessoaDaoJDBC(DB.getConnection());
    }

    public static CRUD<Turma> createTurmaDaoJDBC() {
        return new TurmaDaoJDBC(DB.getConnection());
    }

    public static CRUD<AlunoMatricula> createAlunoMatriculaDaoJDBC() {
        return new AlunoMatriculaDaoJDBC(DB.getConnection());
    }

    public static CRUD<Aluno> createAlunoDaoJDBC() {
        return new AlunoDaoJDBC(DB.getConnection());
    }

    public static CRUD<AlunoContato> createAlunoContatoDaoJDBC() {
        return new AlunoContatoDaoJDBC(DB.getConnection());
    }

    public static CRUD<ProfessorMatricula> createProfessorMatriculaDaoJDBC() {
        return new ProfessorMatriculaDaoJDBC(DB.getConnection());
    }

    public static CRUD<Professor> createProfessorDaoJDBC() {
        return new ProfessorDaoJDBC(DB.getConnection());
    }

    public static CRUD<ProfessorContato> createProfessorContatoDaoJDBC() {
        return new ProfessorContatoDaoJDBC(DB.getConnection());
    }

    public static CRUD<Disciplina> createDisciplinaDaoJDBC() {
        return new DisciplinaDaoJDBC(DB.getConnection());
    }

    public static CRUD<ProfessorDisciplina> createProfessorDisciplinaDaoJDBC() {
        return new ProfessorDisciplinaDaoJDBC(DB.getConnection());
    }

    public static CRUD<ProfessorTurma> createProfessorTurmaDaoJDBC() {
        return new ProfessorTurmaDaoJDBC(DB.getConnection());
    }

    public static CRUD<Funcionario> createFuncionarioDaoJDBC() {
        return new FuncionarioDaoJDBC(DB.getConnection());
    }

    public static CRUD<FuncionarioContato> createFuncionarioContatoDaoJDBC() {
        return new FuncinarioContatoDaoJDBC(DB.getConnection());
    }
}