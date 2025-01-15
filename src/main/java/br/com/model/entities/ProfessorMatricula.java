package br.com.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProfessorMatricula implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String cor_raca;
    private String escolaridade;
    private Date data_matricula;

    public ProfessorMatricula() {}

    public ProfessorMatricula(Integer id, String cor_raca, String escolaridade, Date data_matricula) {
        this.id = id;
        this.cor_raca = cor_raca;
        this.escolaridade = escolaridade;
        this.data_matricula = data_matricula;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCor_raca() {
        return cor_raca;
    }

    public void setCor_raca(String cor_raca) {
        this.cor_raca = cor_raca;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public Date getData_matricula() {
        return data_matricula;
    }

    public void setData_matricula(Date data_matricula) {
        this.data_matricula = data_matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfessorMatricula that = (ProfessorMatricula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProfessorMatricula{" +
                "id=" + id +
                ", cor_raca='" + cor_raca + '\'' +
                ", escolaridade='" + escolaridade + '\'' +
                ", data_matricula=" + data_matricula +
                '}';
    }
}