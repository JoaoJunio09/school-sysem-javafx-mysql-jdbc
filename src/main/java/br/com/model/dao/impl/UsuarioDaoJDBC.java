package br.com.model.dao.impl;

import br.com.db.DB;
import br.com.exceptions.DbException;
import br.com.exceptions.IntegrityDbException;
import br.com.model.dao.CRUD;
import br.com.model.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoJDBC implements CRUD<Usuario> {

    private final Connection conn;

    public UsuarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Usuario obj) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO tb_usuario (Email, Senha, Tipo) VALUES (?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getEmail());
            stmt.setString(2, obj.getSenha());
            stmt.setInt(3, obj.getTipoUsuario());
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
    public void update(Usuario obj) {
        PreparedStatement stmt = null;
        String sql = "UPDATE tb_usuario SET Email = ?, Senha = ?, Tipo = ? WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getEmail());
            stmt.setString(2, obj.getSenha());
            stmt.setInt(3, obj.getTipoUsuario());
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
        String sql = "DELETE FROM tb_usuario WHERE Id = ?";
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
    public Usuario findById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_usuario WHERE Id = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return instantiateUsuario(rs);
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
    public List<Usuario> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_usuario";
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Usuario> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateUsuario(rs));
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
    public List<Usuario> search(String query) {
        return List.of();
    }

    private Usuario instantiateUsuario(ResultSet rs) throws SQLException {
        Usuario obj = new Usuario();
        obj.setId(rs.getInt("Id"));
        obj.setEmail(rs.getString("Email"));
        obj.setSenha(rs.getString("Senha"));
        obj.setTipoUsuario(rs.getInt("Tipo"));
        return obj;
    }
}
