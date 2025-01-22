package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String descricao;

    public Turma() {
    }

    public Turma(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Turma turma = (Turma) o;
        return Objects.equals(id, turma.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nome + " " + descricao;
    }
}