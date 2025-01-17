package br.com.model.services;

import br.com.model.dao.CRUD;
import br.com.model.dao.DaoFactory;
import br.com.model.entities.Usuario;

import java.util.List;

public class LoginService {

    private final CRUD<Usuario> dao = DaoFactory.createUsuarioDaoJDBC();

    public void saveOrUpdate(Usuario obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public List<Usuario> findAll() {
        return dao.findAll();
    }
}
