package br.com.model.dao;

import java.util.List;

public interface CRUD<T> {

    void insert(T obj);
    void update(T obj);
    void deleteById(int id);
    T findById(int id);
    List<T> findAll();
    List<T> search(String query);
}