package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Pessoa;

import java.sql.*;
import java.util.List;

public class PessoaDaoJDBC implements CRUD<Pessoa> {

    private Connection conn = null;

    public PessoaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Pessoa obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_pessoa (Nome, Endereco_res, Complemento, Numero, Bairro, Cep, Email, Data_nasc, Sexo, Cpf, Rg, Naturalidade, Nacionalidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)".formatted();
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEndereco_res());
            stmt.setString(3, obj.getComplemento());
            stmt.setString(4, obj.getNumero());
            stmt.setString(5, obj.getBairro());
            stmt.setString(6, obj.getCep());
            stmt.setString(7, obj.getEmail());
            stmt.setDate(8, new java.sql.Date(obj.getData_nascimento().getTime()));
            stmt.setString(9, obj.getSexo());
            stmt.setString(10, obj.getCpf());
            stmt.setString(11, obj.getRg());
            stmt.setString(12, obj.getNaturalidade());
            stmt.setString(13, obj.getNacionalidade());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);

                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Pessoa obj) {
        
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Pessoa findById(int id) {
        return null;
    }

    @Override
    public List<Pessoa> findAll() {
        return List.of();
    }
}