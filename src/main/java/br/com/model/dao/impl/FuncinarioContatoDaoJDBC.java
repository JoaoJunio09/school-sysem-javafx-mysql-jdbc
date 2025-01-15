package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Pessoa;
import br.com.model.entities.Funcionario;
import br.com.model.entities.FuncionarioContato;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncinarioContatoDaoJDBC implements CRUD<FuncionarioContato> {

    private final Connection conn;

    public FuncinarioContatoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(FuncionarioContato obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_funcionario_contato (Id_funcionario, Descricao, Contato) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getFuncionario().getId());
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
    public void update(FuncionarioContato obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_funcionario_contato SET Id_funcionario = ?, Descricao = ?, Contato = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getFuncionario().getId());
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
            String sql = "DELETE FROM tb_funcionario_contato WHERE Id = ?";
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
    public FuncionarioContato findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT fc.*, f.*, pe.*, " +
                "f.Id AS Funcionario_id, pe.Id AS Pessoa_id " +
                "FROM tb_funcionario_contato fc " +
                "JOIN tb_funcionario f ON fc.Id_Funcionario = f.Id " +
                "JOIN tb_pessoa pe ON f.Id_pessoa = pe.Id " +
                "WHERE fc.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Pessoa pessoa = instantiatePessoa(rs);
                Funcionario funcionario = instantiateFuncionario(rs, pessoa);
                return instantiateFuncionarioContato(rs, funcionario);
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
    public List<FuncionarioContato> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT fc.*, f.*, pe.*, " +
                "f.Id AS Funcionario_id, pe.Id AS Pessoa_id " +
                "FROM tb_funcionario_contato fc " +
                "JOIN tb_funcionario f ON fc.Id_Funcionario = f.Id " +
                "JOIN tb_pessoa pe ON f.Id_pessoa = pe.Id ";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> mapPessoa = new HashMap<>();
            Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
            List<FuncionarioContato> list = new ArrayList<>();

            while (rs.next()) {
                Pessoa pessoa = mapPessoa.get("Id_pessoa");
                if (pessoa == null) {
                    pessoa = instantiatePessoa(rs);
                    mapPessoa.put(rs.getInt("Id_pessoa"), pessoa);
                }

                Funcionario Funcionario = mapFuncionario.get("Id_funcionario");
                if (Funcionario == null) {
                    Funcionario = instantiateFuncionario(rs, pessoa);
                    mapFuncionario.put(rs.getInt("Id_funcionario"), Funcionario);
                }

                list.add(instantiateFuncionarioContato(rs, Funcionario));
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
    public List<FuncionarioContato> search(String query) {
        return List.of();
    }

    private FuncionarioContato instantiateFuncionarioContato(ResultSet rs, Funcionario Funcionario) throws SQLException {
        FuncionarioContato obj = new FuncionarioContato();
        obj.setId(rs.getInt("Id"));
        obj.setFuncionario(Funcionario);
        obj.setDescricao(rs.getString("Descricao"));
        obj.setContato(rs.getString("Contato"));
        return obj;
    }

    private Funcionario instantiateFuncionario(ResultSet rs, Pessoa pessoa) throws SQLException {
        Funcionario obj = new Funcionario();
        obj.setId(rs.getInt("Funcionario_id"));
        obj.setPessoa(pessoa);
        obj.setFiliacao(rs.getString("Filiacao"));
        obj.setN_carteira_trab(rs.getString("N_carteira_trab"));
        obj.setN_ident_pis(rs.getString("N_ident_pis"));
        obj.setData_emissao(rs.getDate("Data_emissao"));
        obj.setData_demissao(rs.getDate("Data_demissao"));
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
