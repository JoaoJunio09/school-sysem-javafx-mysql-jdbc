package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Disciplina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDaoJDBC implements CRUD<Disciplina> {

    private final Connection conn;

    public DisciplinaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Disciplina obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_disciplina (Nome, Descricao) VALUES (?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getDescricao());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
            else throw new IntegrityDbException("No rows affected");
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Disciplina obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_disciplina SET Nome = ?, Descricao = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getDescricao());
            stmt.setInt(3, obj.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement stmt = null;
        String sql = "DELETE FROM tb_disciplina WHERE Id = ?";
        try {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public Disciplina findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_disciplina WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return instantiateDisciplina(rs);
            }

            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public List<Disciplina> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_disciplina";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Disciplina> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateDisciplina(rs));
            }

            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public List<Disciplina> search(String query) {
        return List.of();
    }

    private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
        Disciplina obj = new Disciplina();
        obj.setId(rs.getInt("Id"));
        obj.setNome(rs.getString("Nome"));
        obj.setDescricao(rs.getString("Descricao"));
        return obj;
    }
}
