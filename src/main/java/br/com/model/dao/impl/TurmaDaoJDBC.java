package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDaoJDBC implements CRUD<Turma> {

    private final Connection conn;

    public TurmaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Turma obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_turma (Serie, Turma) VALUES (?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, obj.getSerie());
            stmt.setString(2, obj.getTurma());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else throw new IntegrityDbException("No rows rowsAffected");
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Turma obj) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("UPDATE tb_turma SET Id = ?, Serie = ?, Turma = ? WHERE Id = ?");
            stmt.setInt(1, obj.getId());
            stmt.setString(2, obj.getSerie());
            stmt.setString(3, obj.getTurma());
            stmt.setInt(4, obj.getId());
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
        try {
            stmt = conn.prepareStatement("DELETE FROM tb_turma WHERE Id = ?");
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
    public Turma findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM tb_turma WHERE Id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return instantiateTurma(rs);
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Turma> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM tb_turma");
            rs = stmt.executeQuery();
            List<Turma> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateTurma(rs));
            }

            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Turma> search(String query) {
        return List.of();
    }

    private Turma instantiateTurma(ResultSet rs) throws SQLException {
        Turma obj = new Turma();
        obj.setId(rs.getInt("Id"));
        obj.setSerie(rs.getString("Serie"));
        obj.setTurma(rs.getString("Turma"));
        return obj;
    }
}
