package br.com.model.services;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.AlunoContato;

import java.util.List;

public class AlunoContatoService {

    private final CRUD<AlunoContato> dao = DaoFactory.createAlunoContatoDaoJDBC();

    public void saveOrUpdate(AlunoContato obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public List<AlunoContato> findAll() {
        return dao.findAll();
    }
}
