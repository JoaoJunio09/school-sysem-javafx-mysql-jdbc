package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Funcionario;
import br.com.model.entities.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncionarioDaoJDBC implements CRUD<Funcionario> {

    private final Connection conn;

    public FuncionarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Funcionario obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_funcionario " +
                "(Id_pessoa, Filiacao, N_carteira_trab, N_ident_pis, Data_emissao, Data_demissao) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setString(2, obj.getFiliacao());
            stmt.setString(3, obj.getN_carteira_trab());
            stmt.setString(4, obj.getN_ident_pis());
            stmt.setDate(5, new java.sql.Date(obj.getData_emissao().getTime()));
            stmt.setDate(6, new java.sql.Date(obj.getData_demissao().getTime()));
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
            else throw new IntegrityDbException("NO rows affected");
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Funcionario obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_funcionario " +
                "SET Id_pessoa = ?, Filiacao = ?, N_carteira_trab = ?, N_ident_pis = ?, Data_emissao = ?, Data_demissao = ? " +
                "WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getPessoa().getId());
            stmt.setString(2, obj.getFiliacao());
            stmt.setString(3, obj.getN_carteira_trab());
            stmt.setString(4, obj.getN_ident_pis());
            stmt.setDate(5, new java.sql.Date(obj.getData_emissao().getTime()));
            stmt.setDate(6, new java.sql.Date(obj.getData_demissao().getTime()));
            stmt.setInt(7, obj.getId());
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
        String sql = "DELETE FROM tb_funcionario WHERE Id = ?";
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
    public Funcionario findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT f.*, p.*, " +
                "p.Id AS Pessoa_id " +
                "FROM tb_funcionario f " +
                "JOIN tb_pessoa p ON f.Id_pessoa = p.Id " +
                "WHERE f.Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Pessoa pessoa = instantiatePessoa(rs);
                return instantiateFuncionario(rs, pessoa);
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
    public List<Funcionario> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT f.*, p.*, " +
                "p.Id AS Pessoa_id " +
                "FROM tb_funcionario f " +
                "JOIN tb_pessoa p ON f.Id_pessoa = p.Id ";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            Map<Integer, Pessoa> map = new HashMap<>();
            List<Funcionario> list = new ArrayList<>();

            while (rs.next()) {
                Pessoa pessoa = map.get("Id_pessoa");
                if (pessoa == null) {
                    pessoa = instantiatePessoa(rs);
                    map.put(rs.getInt("Id_pessoa"), pessoa);
                }

                list.add(instantiateFuncionario(rs, pessoa));
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
    public List<Funcionario> search(String query) {
        return List.of();
    }

    private Funcionario instantiateFuncionario(ResultSet rs, Pessoa pessoa) throws SQLException {
        Funcionario obj = new Funcionario();
        obj.setId(rs.getInt("Id"));
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
