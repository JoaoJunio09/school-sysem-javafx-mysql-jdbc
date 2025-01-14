package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.AlunoMatricula;
import br.com.model.entities.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunoMatriculaDaoJDBC implements CRUD<AlunoMatricula> {

    private final Connection conn;

    public AlunoMatriculaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(AlunoMatricula obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_aluno_matricula (Id_turma, Cor_raca, Deficiencia, Tipo_sanguineo, Nece_especiais, Data_matricula, RA) VALUES (?, ?, ?, ?, ?, ?, ?)".formatted();
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getTurma().getId());
            stmt.setString(2, obj.getCor_raca());
            stmt.setString(3, obj.getDeficiencia());
            stmt.setString(4, obj.getTipo_sanguineo());
            stmt.setString(5, obj.getNecessidades_especiais());
            stmt.setDate(6, new java.sql.Date(obj.getData_matricula().getTime()));
            stmt.setString(7, obj.getRA());
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
    public void update(AlunoMatricula obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_aluno_matricula SET Id_turma = ?, Cor_raca = ?, Deficiencia = ?, Tipo_sanguineo = ?, Nece_especiais = ?, Data_matricula = ?, RA = ? WHERE Id = ?".formatted();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getTurma().getId());
            stmt.setString(2, obj.getCor_raca());
            stmt.setString(3, obj.getDeficiencia());
            stmt.setString(4, obj.getTipo_sanguineo());
            stmt.setString(5, obj.getNecessidades_especiais());
            stmt.setDate(6, new java.sql.Date(obj.getData_matricula().getTime()));
            stmt.setString(7, obj.getRA());
            stmt.setInt(8, obj.getId());
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
        String sql = "DELETE FROM tb_aluno_matricula WHERE Id = ?";
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
    public AlunoMatricula findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT am.*, t.*, t.Id AS Turma_id " +
                "FROM tb_aluno_matricula am " +
                "JOIN tb_turma t ON am.Id_turma = t.Id " +
                "WHERE am.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Turma turma = instantiateTurma(rs);
                AlunoMatricula obj = instantiateAlunoMatricula(rs, turma);
                return obj;
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
    public List<AlunoMatricula> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT am.*, t.*, t.Id AS Turma_id " +
                "FROM tb_aluno_matricula am " +
                "JOIN tb_turma t ON am.Id_turma = t.Id ";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Turma> map = new HashMap<>();
            List<AlunoMatricula> list = new ArrayList<>();

            while (rs.next()) {
                Turma turma = map.get(rs.getInt("Id_turma"));

                if (turma == null) {
                    turma = instantiateTurma(rs);
                    map.put(rs.getInt("Id_turma"), turma);
                }

                AlunoMatricula obj = instantiateAlunoMatricula(rs, turma);
                list.add(obj);
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
    public List<AlunoMatricula> search(String query) {
        return List.of();
    }

    private Turma instantiateTurma(ResultSet rs) throws SQLException {
        Turma obj = new Turma();
        obj.setId(rs.getInt("Turma_id"));
        obj.setNome(rs.getString("Nome"));
        obj.setDescricao(rs.getString("Descricao"));
        return obj;
    }

    private AlunoMatricula instantiateAlunoMatricula(ResultSet rs, Turma turma) throws SQLException {
        AlunoMatricula obj = new AlunoMatricula();
        obj.setId(rs.getInt("Id"));
        obj.setTurma(turma);
        obj.setCor_raca(rs.getString("Cor_raca"));
        obj.setDeficiencia(rs.getString("Deficiencia"));
        obj.setTipo_sanguineo(rs.getString("Tipo_sanguineo"));
        obj.setNecessidades_especiais(rs.getString("Nece_especiais"));
        obj.setData_matricula(rs.getDate("Data_matricula"));
        obj.setRA(rs.getString("RA"));
        return obj;
    }
}
