package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Professor;
import br.com.model.entities.ProfessorContato;
import br.com.model.entities.ProfessorMatricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorContatoDaoJDBC implements CRUD<ProfessorContato> {

    private final Connection conn;

    public ProfessorContatoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ProfessorContato obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_professor_contato (Id_professor, Descricao, Contato) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setString(2, obj.getDescricao());
            stmt.setString(3, obj.getContato());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(ProfessorContato obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_professor_contato SET Id_professor = ?, Descricao = ?, Contato = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getProfessor().getId());
            stmt.setString(2, obj.getDescricao());
            stmt.setString(3, obj.getContato());
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
            String sql = "DELETE FROM tb_professor_contato WHERE Id = ?";
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
    public ProfessorContato findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pc.*, p.*, pe.*, pm.*, " +
                "p.Id AS Professor_id, pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor_contato pc " +
                "JOIN tb_professor p ON pc.Id_professor = p.Id " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id " +
                "WHERE pc.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                ProfessorMatricula professorMatricula = instantiateProfessorMatricula(rs);
                Pessoa pessoa = instantiatePessoa(rs);
                Professor professor = instantiateProfessor(rs, pessoa, professorMatricula);
                return instantiateProfessorContato(rs, professor);
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
    public List<ProfessorContato> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pc.*, p.*, pe.*, pm.*, " +
                "p.Id AS Professor_id, pe.Id AS Pessoa_id, pm.Id AS Professor_matricula_id " +
                "FROM tb_professor_contato pc " +
                "JOIN tb_professor p ON pc.Id_professor = p.Id " +
                "JOIN tb_pessoa pe ON p.Id_pessoa = pe.Id " +
                "JOIN tb_professor_matricula pm ON p.Id_professor_matricula = pm.Id";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, ProfessorMatricula> mapProfMatricula = new HashMap<>();
            Map<Integer, Professor> mapProfessor = new HashMap<>();
            List<ProfessorContato> list = new ArrayList<>();

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

                list.add(instantiateProfessorContato(rs, professor));
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
    public List<ProfessorContato> search(String query) {
        return List.of();
    }

    private ProfessorContato instantiateProfessorContato(ResultSet rs, Professor professor) throws SQLException {
        ProfessorContato obj = new ProfessorContato();
        obj.setId(rs.getInt("Id"));
        obj.setProfessor(professor);
        obj.setDescricao(rs.getString("Descricao"));
        obj.setContato(rs.getString("Contato"));
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
