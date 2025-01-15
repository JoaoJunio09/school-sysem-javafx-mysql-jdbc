package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.ProfessorMatricula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorMatriculaDaoJDBC implements CRUD<ProfessorMatricula> {

    private final Connection conn;

    public ProfessorMatriculaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ProfessorMatricula obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_professor_matricula (Cor_raca, Escolaridade, Data_matricula) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getCor_raca());
            stmt.setString(2, obj.getEscolaridade());
            stmt.setDate(3, new java.sql.Date(obj.getData_matricula().getTime()));
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
    public void update(ProfessorMatricula obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_professor_matricula SET Cor_raca = ?, Escolaridade = ?, Data_matricula = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getCor_raca());
            stmt.setString(2, obj.getEscolaridade());
            stmt.setDate(3, new java.sql.Date(obj.getData_matricula().getTime()));
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
        String sql = "DELETE FROM tb_professor_matricula WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
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
    public ProfessorMatricula findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_professor_matricula WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) return instantiateProfessorMatricula(rs);

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
    public List<ProfessorMatricula> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_professor_matricula";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<ProfessorMatricula> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateProfessorMatricula(rs));
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
    public List<ProfessorMatricula> search(String query) {
        return List.of();
    }

    private ProfessorMatricula instantiateProfessorMatricula(ResultSet rs) throws SQLException {
        ProfessorMatricula obj = new ProfessorMatricula();
        obj.setId(rs.getInt("Id"));
        obj.setCor_raca(rs.getString("Cor_raca"));
        obj.setEscolaridade(rs.getString("Escolaridade"));
        obj.setData_matricula(rs.getDate("Data_matricula"));
        return obj;
    }
}
