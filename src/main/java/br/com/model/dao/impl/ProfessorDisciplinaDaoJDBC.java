package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorDisciplinaDaoJDBC implements CRUD<ProfessorDisciplina> {

    private final Connection conn;

    public ProfessorDisciplinaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ProfessorDisciplina obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_professor_disciplina (Id_professor, Id_disciplina) VALUES (?,?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setInt(2, obj.getDisciplina().getId());
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
    public void update(ProfessorDisciplina obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_professor_disciplina SET Id_professor = ?, Id_disciplina = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setInt(2, obj.getDisciplina().getId());
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
        String sql = "DELETE FROM tb_professor_disciplina WHERE Id = ?";
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
    public ProfessorDisciplina findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pd.*, p.*, d.*, pe.*, pm.*, " +
                "p.Id AS Professor_id, d.Id AS Disciplina_id, d.Nome AS Disciplina_nome, pe.Nome AS Pessoa_nome, " +
                "pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor_disciplina pd " +
                "JOIN tb_professor p ON pd.Id_professor = p.Id " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id " +
                "JOIN tb_disciplina d ON pd.Id_disciplina = d.Id " +
                "WHERE pd.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Pessoa pessoa = instantiatePessoa(rs);
                ProfessorMatricula professorMatricula = instantiateProfessorMatricula(rs);
                Professor professor = instantiateProfessor(rs, pessoa, professorMatricula);
                Disciplina disciplina = instantiateDisciplina(rs);
                return instantiateProfessorDisciplina(rs, professor, disciplina);
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
    public List<ProfessorDisciplina> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pd.*, p.*, d.*, pe.*, pm.*, " +
                "p.Id AS Professor_id, d.Id AS Disciplina_id, d.Nome AS Disciplina_nome, pe.Nome AS Pessoa_nome, " +
                "pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor_disciplina pd " +
                "JOIN tb_professor p ON pd.Id_professor = p.Id " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id " +
                "JOIN tb_disciplina d ON pd.Id_disciplina = d.Id";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, ProfessorMatricula> mapProfMatricula = new HashMap<>();
            Map<Integer, Professor> mapProfessor = new HashMap<>();
            Map<Integer, Disciplina> mapDisciplina = new HashMap<>();
            List<ProfessorDisciplina> list = new ArrayList<>();

            while (rs.next()) {
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

                Professor professor = mapProfessor.get("Id_professor");
                if (professor == null) {
                    professor = instantiateProfessor(rs, pessoa, professorMatricula);
                    mapProfessor.put(rs.getInt("Id_professor"), professor);
                }

                Disciplina disciplina = mapDisciplina.get("Id_disciplina");
                if (disciplina == null) {
                    disciplina = instantiateDisciplina(rs);
                    mapDisciplina.put(rs.getInt("Id_disciplina"), disciplina);
                }

                list.add(instantiateProfessorDisciplina(rs, professor, disciplina));
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
    public List<ProfessorDisciplina> search(String query) {
        return List.of();
    }

    private ProfessorDisciplina instantiateProfessorDisciplina(ResultSet rs, Professor profesor, Disciplina disciplina) throws SQLException {
        ProfessorDisciplina obj = new ProfessorDisciplina();
        obj.setId(rs.getInt("Id"));
        obj.setDisciplina(disciplina);
        obj.setProfessor(profesor);
        return obj;
    }

    private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
        Disciplina obj = new Disciplina();
        obj.setId(rs.getInt("Disciplina_id"));
        obj.setNome(rs.getString("Disciplina_nome"));
        obj.setDescricao(rs.getString("Descricao"));
        return obj;
    }

    private Professor instantiateProfessor(ResultSet rs, Pessoa pessoa, ProfessorMatricula professorMatricula) throws SQLException {
        Professor obj = new Professor();
        obj.setId(rs.getInt("Professor_id"));
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
        obj.setNome(rs.getString("Pessoa_nome"));
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
