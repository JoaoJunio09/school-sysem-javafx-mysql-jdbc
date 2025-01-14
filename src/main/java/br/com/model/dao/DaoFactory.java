package br.com.model.dao;

import br.com.db.DB;
import br.com.model.dao.impl.AlunoDaoJDBC;
import br.com.model.dao.impl.AlunoMatriculaDaoJDBC;
import br.com.model.dao.impl.PessoaDaoJDBC;
import br.com.model.dao.impl.TurmaDaoJDBC;
import br.com.model.entities.Aluno;
import br.com.model.entities.AlunoMatricula;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Turma;

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
}