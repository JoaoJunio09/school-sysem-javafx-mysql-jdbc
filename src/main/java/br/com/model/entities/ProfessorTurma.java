package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProfessorTurma implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Professor professor;
    private Turma turma;
    private Disciplina disciplina;

    public ProfessorTurma() {}

    public ProfessorTurma(Integer id, Professor professor, Turma turma, Disciplina disciplina) {
        this.id = id;
        this.professor = professor;
        this.turma = turma;
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

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma Turma) {
        this.turma = turma;
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
        ProfessorTurma that = (ProfessorTurma) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProfessorTurma{" +
                "id=" + id +
                ", professor=" + professor +
                ", turma=" + turma +
                '}';
    }
}
