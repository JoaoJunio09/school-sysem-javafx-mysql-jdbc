package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProfessorContato implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Professor professor;
    private String descricao;
    private String contato;

    public ProfessorContato() {}

    public ProfessorContato(Integer id, Professor professor, String descricao, String contato) {
        this.id = id;
        this.professor = professor;
        this.descricao = descricao;
        this.contato = contato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
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
        ProfessorContato that = (ProfessorContato) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProfessorContato{" +
                "id=" + id +
                ", professor=" + professor +
                ", descricao='" + descricao + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
