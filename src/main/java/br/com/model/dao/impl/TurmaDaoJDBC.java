package br.com.model.dao.impl;

import br.com.model.dao.CRUD;
import br.com.model.entities.Turma;

import java.sql.Connection;
import java.util.List;

public class TurmaDaoJDBC implements CRUD<Turma> {

    private Connection conn = null;

    public TurmaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Turma obj) {

    }

    @Override
    public void update(Turma obj) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Turma findById(int id) {
        return null;
    }

    @Override
    public List<Turma> findAll() {
        return List.of();
    }
}
