package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Professor;
import br.com.model.entities.ProfessorMatricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorDaoJDBC implements CRUD<Professor> {

    private final Connection conn;

    public ProfessorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Professor obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_professor (Id_pessoa, Id_professor_matricula) VALUES (?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setInt(2, obj.getProfessor_matricula().getId());
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
    public void update(Professor obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_professor SET Id_pessoa = ?, Id_professor_matricula = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setInt(2, obj.getProfessor_matricula().getId());
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
        String sql = "DELETE FROM tb_professor WHERE Id = ?";
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
    public Professor findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT p.*, pe.*, pm.*, " +
                "pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor p " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id " +
                "WHERE p.Id = ? ";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Pessoa pessoa = instantiatePessoa(rs);
                ProfessorMatricula professorMatricula = instantiateProfessorMatricula(rs);
                return instantiateProfessor(rs, pessoa, professorMatricula);
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
    public List<Professor> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT p.*, pe.*, pm.*, " +
                "pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor p " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, ProfessorMatricula> mapProfMatricula = new HashMap<>();
            List<Professor> list = new ArrayList<>();

            if (rs.next()) {
                Pessoa pessoa = mapPessoa.get("Id_pessoa");
                if (pessoa == null) {
                    pessoa = instantiatePessoa(rs);
                    mapPessoa.put(rs.getInt("Id_pessoa"), pessoa);
                }

                ProfessorMatricula professorMatricula  = mapProfMatricula.get("Id_professor_matricula");
                if (professorMatricula == null) {
                    professorMatricula = instantiateProfessorMatricula(rs);
                    mapProfMatricula.put(rs.getInt("Id_professor_matricula"), professorMatricula);
                }

                list.add(instantiateProfessor(rs, pessoa, professorMatricula));
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
    public List<Professor> search(String query) {
        return List.of();
    }

    private Professor instantiateProfessor(ResultSet rs, Pessoa pessoa, ProfessorMatricula professorMatricula) throws SQLException {
        Professor obj = new Professor();
        obj.setId(rs.getInt("Id"));
        obj.setPessoa(pessoa);
        obj.setProfessor_matricula(professorMatricula);
        return obj;
    }

    private ProfessorMatricula instantiateProfessorMatricula(ResultSet rs) throws SQLException {
        ProfessorMatricula obj = new ProfessorMatricula();
        obj.setId(rs.getInt("Professor_matricula_id"));
        obj.setCor_raca(rs.getString("Cor_raca"));
        obj.setEscolaridade(rs.getString("Escolaridade"));
        obj.setData_matricula(rs.getDate("Data_matricula"));
        return obj;
    }

    private Pessoa instantiatePessoa(ResultSet rs) throws SQLException {
        Pessoa obj = new Pessoa();
        obj.setId(rs.getInt("Pessoa_id"));
        obj.setNome(rs.getString("Nome"));
        obj.setEndereco_res(rs.getString("Endereco_res"));
        obj.setComplemento(rs.getString("Complemento"));
        obj.setNumero(rs.getString("Numero"));
        obj.setBairro(rs.getString("Bairro"));
        obj.setCep(rs.getString("Cep"));
        obj.setEmail(rs.getString("Email"));
        obj.setData_nascimento(rs.getDate("Data_nasc"));
        obj.setSexo(rs.getString("Sexo"));
        obj.setCpf(rs.getString("Cpf"));
        obj.setRg(rs.getString("Rg"));
        obj.setNaturalidade(rs.getString("Naturalidade"));
        obj.setNacionalidade(rs.getString("Nacionalidade"));
        return obj;
    }
}
