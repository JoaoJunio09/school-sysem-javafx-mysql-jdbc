package br.com.model.services;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.AlunoMatricula;

public class AlunoMatriculaService {

    private final CRUD<AlunoMatricula> dao = DaoFactory.createAlunoMatriculaDaoJDBC();

    public void saveOrUpdate(AlunoMatricula obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }
}
