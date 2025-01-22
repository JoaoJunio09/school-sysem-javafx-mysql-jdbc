package br.com.model.services;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.Turma;

import java.util.List;

public class TurmaService {

    private final CRUD<Turma> dao = DaoFactory.createTurmaDaoJDBC();

    public void saveOrUpdate(Turma obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public List<Turma> findAll() {
        return dao.findAll();
    }
}
