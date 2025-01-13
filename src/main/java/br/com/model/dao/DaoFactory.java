package br.com.model.dao;

import br.com.db.DB;
import br.com.model.dao.impl.PessoaDaoJDBC;
import br.com.model.dao.impl.TurmaDaoJDBC;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Turma;

public class DaoFactory {

    public static CRUD<Pessoa> createPessoaDaoJDBC() {
        return new PessoaDaoJDBC(DB.getConnectio());
    }
    public static CRUD<Turma> createTurmaDaoJDBC() { return new TurmaDaoJDBC(DB.getConnectio()); }
}
