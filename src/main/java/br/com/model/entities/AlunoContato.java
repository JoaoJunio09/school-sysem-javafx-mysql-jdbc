package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class AlunoContato implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Aluno aluno;
    private String  descricao;
    private String  contato;

    public AlunoContato() {}

    public AlunoContato(Integer id, Aluno aluno, String descricao, String contato) {
        this.id = id;
        this.aluno = aluno;
        this.descricao = descricao;
        this.contato = contato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
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
        AlunoContato that = (AlunoContato) o;
        return Objects.equals(id, that.id) && Objects.equals(aluno, that.aluno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aluno);
    }

    @Override
    public String toString() {
        return "AlunoContato{" +
                "id=" + id +
                ", aluno=" + aluno +
                ", descricao='" + descricao + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
