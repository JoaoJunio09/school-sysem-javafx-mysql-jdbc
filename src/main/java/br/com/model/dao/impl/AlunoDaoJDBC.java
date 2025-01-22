package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Aluno;
import br.com.model.entities.AlunoMatricula;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunoDaoJDBC implements CRUD<Aluno> {

    private final Connection conn;

    public AlunoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Aluno obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_aluno (Id_pessoa, Id_aluno_matricula) VALUES (?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setInt(2, obj.getAluno_matricula().getId());
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
    public void update(Aluno obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_aluno SET Id_pessoa = ?, Id_aluno_matricula = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setInt(2, obj.getAluno_matricula().getId());
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
        String sql = "DELETE FROM tb_aluno WHERE Id = ?";
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
    public Aluno findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT a.*, p.*, am.*, t.*, " +
                "p.Id AS Pessoa_id, am.Id AS Aluno_matricula_id, t.Id AS Turma_id, " +
                "t.Nome AS Turma_nome " +
                "FROM tb_aluno a " +
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
                Aluno obj = instantiateAluno(rs, pessoa, aluno_matricula);
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
    public List<Aluno> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT a.*, p.*, am.*, t.*, " +
                "p.Id AS Pessoa_id, am.Id AS Aluno_matricula_id, t.Id AS Turma_id, " +
                "t.Serie AS Turma_nome " +
                "FROM tb_aluno a " +
                "JOIN tb_pessoa p ON a.Id_pessoa = p.Id " +
                "JOIN tb_aluno_matricula am ON a.Id_aluno_matricula = am.Id " +
                "JOIN tb_turma t ON am.Id_turma = t.Id";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Turma> mapTurma = new HashMap<>();
            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, AlunoMatricula> mapAlunoMatricula = new HashMap<>();
            List<Aluno> list = new ArrayList<>();

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

                Aluno obj = instantiateAluno(rs, pessoa, aluno_matricula);
                list.add(instantiateAluno(rs, pessoa, aluno_matricula));
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
    public List<Aluno> search(String query) {
        return List.of();
    }

    private Aluno instantiateAluno(ResultSet rs, Pessoa pessoa, AlunoMatricula aluno_matricula) throws SQLException {
        Aluno obj = new Aluno();
        obj.setId(rs.getInt("Id"));
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
        obj.setTurma(rs.getString("Turma"));
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
