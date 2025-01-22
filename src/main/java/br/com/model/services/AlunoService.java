package br.com.model.services;

import br.com.exceptions.UserAlreadyExists;
import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.Aluno;

import java.util.List;

public class AlunoService {

    private final CRUD<Aluno> dao = DaoFactory.createAlunoDaoJDBC();

    public void saveOrUpdate(Aluno obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public List<Aluno> findAll() {
        return dao.findAll();
    }
}
