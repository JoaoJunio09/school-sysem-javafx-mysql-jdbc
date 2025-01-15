package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProfessorDisciplina implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Professor professor;
    private Disciplina disciplina;

    public ProfessorDisciplina() {}

    public ProfessorDisciplina(Integer id, Professor professor, Disciplina disciplina) {
        this.id = id;
        this.professor = professor;
        this.disciplina = disciplina;
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

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfessorDisciplina that = (ProfessorDisciplina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProfessorDisciplina{" +
                "id=" + id +
                ", professor=" + professor +
                ", disciplina=" + disciplina +
                '}';
    }
}
