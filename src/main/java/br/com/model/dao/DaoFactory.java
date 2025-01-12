package br.com.model.dao;

import br.com.db.DB;
import br.com.model.dao.impl.PessoaDaoJDBC;
import br.com.model.entities.Pessoa;

public class DaoFactory {

    public static CRUD<Pessoa> createPessoaDaoJDBC() {
        return new PessoaDaoJDBC(DB.getConnectio());
    }
}
