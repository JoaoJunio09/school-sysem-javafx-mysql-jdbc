package br.com.model.services;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.Pessoa;

import java.util.List;

public class PessoaService {
    private final CRUD<Pessoa> dao = DaoFactory.createPessoaDaoJDBC();

    public void saveOrUpdate(Pessoa obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void deleteById(Pessoa obj) {
        if (obj == null) throw new IllegalStateException("Obj was null");

        dao.deleteById(obj.getId());
    }

    public Pessoa findById(Pessoa obj) {
        if (obj == null) throw new IllegalStateException("Obj was null");

        return dao.findById(obj.getId());
    }

    public List<Pessoa> findAll() {
        return dao.findAll();
    }

}
