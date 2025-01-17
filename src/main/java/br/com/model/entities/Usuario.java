package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String senha;
    private Integer tipoUsuario;

    /*
    1 - Secretaria
    2 - Aluno
    3 - Professor
    4 - Gestão Escolar
    */

    public Usuario() {}

    public Usuario(Integer id, String email, String senha, Integer tipoUsuario) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email) && Objects.equals(senha, usuario.senha) && tipoUsuario == usuario.tipoUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, senha, tipoUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
