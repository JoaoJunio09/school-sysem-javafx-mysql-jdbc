package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class FuncionarioContato implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Funcionario funcionario;
    private String descricao;
    private String contato;

    public FuncionarioContato() {}

    public FuncionarioContato(Integer id, Funcionario funcionario, String descricao, String contato) {
        this.id = id;
        this.funcionario = funcionario;
        this.descricao = descricao;
        this.contato = contato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FuncionarioContato that = (FuncionarioContato) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FuncionarioContato{" +
                "id=" + id +
                ", funcionario=" + funcionario +
                ", descricao='" + descricao + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
