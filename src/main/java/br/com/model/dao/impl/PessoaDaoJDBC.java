package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDaoJDBC implements CRUD<Pessoa> {

    private Connection conn = null;

    public PessoaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Pessoa obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_pessoa (Nome, Endereco_res, Complemento, Numero, Bairro, Cep, Email, Data_nasc, Sexo, Cpf, Rg, Naturalidade, Nacionalidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)".formatted();
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEndereco_res());
            stmt.setString(3, obj.getComplemento());
            stmt.setString(4, obj.getNumero());
            stmt.setString(5, obj.getBairro());
            stmt.setString(6, obj.getCep());
            stmt.setString(7, obj.getEmail());
            stmt.setDate(8, new java.sql.Date(obj.getData_nascimento().getTime()));
            stmt.setString(9, obj.getSexo());
            stmt.setString(10, obj.getCpf());
            stmt.setString(11, obj.getRg());
            stmt.setString(12, obj.getNaturalidade());
            stmt.setString(13, obj.getNacionalidade());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);

                    obj.setId(id);
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
    public void update(Pessoa obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_pessoa SET Nome = ?, Endereco_res = ?, Complemento = ?, Numero = ?, Bairro = ?, Cep = ?, Email = ?, Data_nasc = ?, Sexo = ?, Cpf = ?, Rg = ?, Naturalidade = ?, Nacionalidade = ? WHERE Id = ?".formatted();
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEndereco_res());
            stmt.setString(3, obj.getComplemento());
            stmt.setString(4, obj.getNumero());
            stmt.setString(5, obj.getBairro());
            stmt.setString(6, obj.getCep());
            stmt.setString(7, obj.getEmail());
            stmt.setDate(8, new java.sql.Date(obj.getData_nascimento().getTime()));
            stmt.setString(9, obj.getSexo());
            stmt.setString(10, obj.getCpf());
            stmt.setString(11, obj.getRg());
            stmt.setString(12, obj.getNaturalidade());
            stmt.setString(13, obj.getNacionalidade());
            stmt.setInt(14, obj.getId());

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
        try {
            stmt = conn.prepareStatement("DELETE FROM tb_pessoa WHERE Id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException  e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public Pessoa findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM tb_pessoa WHERE Id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return instantiatePessoa(rs);
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
    public List<Pessoa> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM tb_pessoa");
            rs = stmt.executeQuery();
            List<Pessoa> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiatePessoa(rs));
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
    public List<Pessoa> search(String query) {
        return List.of();
    }

    private Pessoa instantiatePessoa(ResultSet rs) throws SQLException {
        Pessoa obj = new Pessoa();
        obj.setId(rs.getInt("Id"));
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