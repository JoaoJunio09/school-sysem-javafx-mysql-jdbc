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

public class AlunoContatoDaoJDBC implements CRUD<AlunoContato> {

    private final Connection conn;

    public AlunoContatoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(AlunoContato obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_aluno_contato (Id_aluno, Descricao, Contato) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getAluno().getId());
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
    public void update(AlunoContato obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_aluno_contato SET Id_aluno = ?, Descricao = ?, Contato = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getAluno().getId());
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
        String sql = "DELETE FROM tb_aluno_contato WHERE Id = ?";
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
    public AlunoContato findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT ac.*, a.*, p.*, am.*, t.*, " +
                "p.Id AS Pessoa_id, am.Id AS Aluno_matricula_id, t.Id AS Turma_id, a.Id AS Aluno_id, " +
                "t.Nome AS Turma_nome, t.Descricao AS Descricao_turma " +
                "FROM tb_aluno_contato ac " +
                "JOIN tb_aluno a ON ac.Id_aluno = a.Id " +
                "JOIN tb_pessoa p ON a.Id_pessoa = p.Id " +
                "JOIN tb_aluno_matricula am ON a.Id_aluno_matricula = am.Id " +
                "JOIN tb_turma t ON am.Id_turma = t.Id " +
                "WHERE a.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Turma turma = instantiateTurma(rs);
                AlunoMatricula aluno_matricula = instantiateAlunoMatricula(rs, turma);
                Pessoa pessoa = instantiatePessoa(rs);
                Aluno aluno = instantiateAluno(rs, pessoa, aluno_matricula);
                AlunoContato obj = instantiateAlunoContato(rs, aluno);
                return obj;
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
    public List<AlunoContato> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT ac.*, a.*, p.*, am.*, t.*, " +
                "p.Id AS Pessoa_id, am.Id AS Aluno_matricula_id, t.Id AS Turma_id, a.Id AS Aluno_id, " +
                "t.Nome AS Turma_nome, t.Descricao AS Descricao_turma " +
                "FROM tb_aluno_contato ac " +
                "JOIN tb_aluno a ON ac.Id_aluno = a.Id " +
                "JOIN tb_pessoa p ON a.Id_pessoa = p.Id " +
                "JOIN tb_aluno_matricula am ON a.Id_aluno_matricula = am.Id " +
                "JOIN tb_turma t ON am.Id_turma = t.Id ";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Turma> mapTurma = new HashMap<>();
            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, AlunoMatricula> mapAlunoMatricula = new HashMap<>();
            Map<Integer, Aluno> mapAluno = new HashMap<>();
            List<AlunoContato> list = new ArrayList<>();

            while (rs.next()) {

                Turma turma = mapTurma.get("Id_turma");
                if (turma == null) {
                    turma = instantiateTurma(rs);
                    mapTurma.put(rs.getInt("Id_turma"), turma);
                }

                Pessoa pessoa = mapPessoa.get("Id_pessoa");
                if (pessoa == null) {
                    pessoa = instantiatePessoa(rs);
                    mapPessoa.put(rs.getInt("Id_pessoa"), pessoa);
                }

                AlunoMatricula aluno_matricula = mapAlunoMatricula.get("Id_aluno_matricula");
                if (aluno_matricula == null) {
                    aluno_matricula = instantiateAlunoMatricula(rs, turma);
                    mapAlunoMatricula.put(rs.getInt("Id_aluno_matricula"), aluno_matricula);
                }

                Aluno aluno = mapAluno.get("Id_aluno");
                if (aluno == null) {
                    aluno = instantiateAluno(rs, pessoa, aluno_matricula);
                    mapAluno.put(rs.getInt("Id_aluno"), aluno);
                }

                list.add(instantiateAlunoContato(rs, aluno));
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
    public List<AlunoContato> search(String query) {
        return List.of();
    }

    private AlunoContato instantiateAlunoContato(ResultSet rs, Aluno aluno) throws SQLException {
        AlunoContato obj = new AlunoContato();
        obj.setId(rs.getInt("Id"));
        obj.setAluno(aluno);
        obj.setDescricao(rs.getString("Descricao"));
        obj.setContato(rs.getString("Contato"));
        return obj;
    }

    private Aluno instantiateAluno(ResultSet rs, Pessoa pessoa, AlunoMatricula aluno_matricula) throws SQLException {
        Aluno obj = new Aluno();
        obj.setId(rs.getInt("Aluno_id"));
        obj.setPessoa(pessoa);
        obj.setAluno_matricula(aluno_matricula);
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

    private Turma instantiateTurma(ResultSet rs) throws SQLException {
        Turma obj = new Turma();
        obj.setId(rs.getInt("Turma_id"));
        obj.setSerie(rs.getString("Turma_nome"));
        obj.setTurma(rs.getString("Descricao_turma"));
        return obj;
    }

    private AlunoMatricula instantiateAlunoMatricula(ResultSet rs, Turma turma) throws SQLException {
        AlunoMatricula obj = new AlunoMatricula();
        obj.setId(rs.getInt("Aluno_matricula_id"));
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
