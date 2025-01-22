package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProfessorTurma implements Serializable {
    private static final long serialVersionUID = 1L;

    private Professor professor;
    private Turma turma;
    private Disciplina disciplina;

    public ProfessorTurma() {}

    public ProfessorTurma(Professor professor, Turma turma, Disciplina disciplina) {
        this.professor = professor;
        this.turma = turma;
        this.disciplina = disciplina;
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

    public void setTurma(Turma turma) {
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
        return Objects.equals(professor, that.professor) && Objects.equals(turma, that.turma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professor, turma);
    }

    @Override
    public String toString() {
        return "ProfessorTurma{" +
                "professor=" + professor +
                ", turma=" + turma +
                ", disciplina=" + disciplina +
                '}';
    }
}
