package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Aluno implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Pessoa pessoa;
    private AlunoMatricula aluno_matricula;

    public Aluno() {}

    public Aluno(Integer id, Pessoa pessoa, AlunoMatricula aluno_matricula) {
        this.id = id;
        this.pessoa = pessoa;
        this.aluno_matricula = aluno_matricula;
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

    public AlunoMatricula getAluno_matricula() {
        return aluno_matricula;
    }

    public void setAluno_matricula(AlunoMatricula aluno_matricula) {
        this.aluno_matricula = aluno_matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(id, aluno.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                ", aluno_matricula=" + aluno_matricula +
                '}';
    }
}
