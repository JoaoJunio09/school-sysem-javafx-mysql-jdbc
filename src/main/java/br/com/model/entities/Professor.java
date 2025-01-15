package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Professor implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Pessoa pessoa;
    private ProfessorMatricula professor_matricula;

    public Professor() {}

    public Professor(Integer id, Pessoa pessoa, ProfessorMatricula professor_matricula) {
        this.id = id;
        this.pessoa = pessoa;
        this.professor_matricula = professor_matricula;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ProfessorMatricula getProfessor_matricula() {
        return professor_matricula;
    }

    public void setProfessor_matricula(ProfessorMatricula professor_matricula) {
        this.professor_matricula = professor_matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(id, professor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                ", professor_matricula=" + professor_matricula +
                '}';
    }
}
