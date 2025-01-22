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

public class ProfessorTurmaDaoJDBC implements CRUD<ProfessorTurma> {

    private final Connection conn;

    public ProfessorTurmaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ProfessorTurma obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_professor_turma (Id_professor, Id_turma, Id_disciplina) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setInt(2, obj.getTurma().getId());
            stmt.setInt(3, obj.getDisciplina().getId());
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
    public void update(ProfessorTurma obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_professor_turma SET Id_professor = ?, Id_turma = ?, Id_disciplina = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setInt(2, obj.getTurma().getId());
            stmt.setInt(3, obj.getDisciplina().getId());
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
        String sql = "DELETE FROM tb_professor_turma WHERE Id = ?";
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
    public ProfessorTurma findById(int id) {
        return null;
    }

    @Override
    public List<ProfessorTurma> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pt.*, p.*, pe.*, pm.*, t.*, d.*, " +
                "p.Id AS Professor_id, pe.Id AS Pessoa_id, pe.Nome AS Pessoa_nome, " +
                "pm.Id AS Professor_matricula_id, t.Id AS Turma_id, t.Nome AS Turma_nome, " +
                "d.Id AS Disciplina_id, d.Nome AS Disciplina_nome, d.Descricao AS Disciplina_descricao " +
                "FROM tb_professor_turma pt " +
                "JOIN tb_professor p ON pt.Id_professor = p.Id " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id " +
                "JOIN tb_turma t ON pt.Id_turma = t.Id " +
                "JOIN tb_disciplina d ON pt.Id_disciplina = d.Id";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, ProfessorMatricula> mapProfMatricula = new HashMap<>();
            Map<Integer, Professor> mapProfessor = new HashMap<>();
            Map<Integer, Turma> mapTurma = new HashMap<>();
            Map<Integer, Disciplina> mapDisciplina = new HashMap<>();
            List<ProfessorTurma> list = new ArrayList<>();

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

                Turma Turma = mapTurma.get("Id_turma");
                if (Turma == null) {
                    Turma = instantiateTurma(rs);
                    mapTurma.put(rs.getInt("Id_turma"), Turma);
                }

                Disciplina disciplina = mapDisciplina.get("Id_disciplina");
                if (disciplina == null) {
                    disciplina = instantiateDisciplina(rs);
                    mapDisciplina.put(rs.getInt("Id_disciplina"), disciplina);
                }

                list.add(instantiateProfessorTurma(rs, professor, Turma, disciplina));
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
    public List<ProfessorTurma> search(String query) {
        return List.of();
    }

    private ProfessorTurma instantiateProfessorTurma(ResultSet rs, Professor profesor, Turma Turma, Disciplina disciplina) throws SQLException {
        ProfessorTurma obj = new ProfessorTurma();
        obj.setProfessor(profesor);
        obj.setTurma(Turma);
        obj.setDisciplina(disciplina);
        return obj;
    }

    private Turma instantiateTurma(ResultSet rs) throws SQLException {
        Turma obj = new Turma();
        obj.setId(rs.getInt("Turma_id"));
        obj.setSerie(rs.getString("Turma_nome"));
        obj.setTurma(rs.getString("Turma"));
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

    private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
        Disciplina obj = new Disciplina();
        obj.setId(rs.getInt("Disciplina_id"));
        obj.setNome(rs.getString("Disciplina_nome"));
        obj.setDescricao(rs.getString("Disciplina_descricao"));
        return obj;
    }
}
