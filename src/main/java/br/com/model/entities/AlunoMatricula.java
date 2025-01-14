package br.com.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AlunoMatricula implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Turma turma;
    private String cor_raca;
    private String deficiencia;
    private String tipo_sanguineo;
    private String necessidades_especiais;
    private Date data_matricula;
    private String RA;

    public AlunoMatricula() {}

    public AlunoMatricula(Integer id, Turma turma, String cor_raca, String deficiencia, String tipo_sanguineo,
        String necessidades_especiais, Date data_matricula, String RA
        ) {
        this.id = id;
        this.turma = turma;
        this.cor_raca = cor_raca;
        this.deficiencia = deficiencia;
        this.tipo_sanguineo = tipo_sanguineo;
        this.necessidades_especiais = necessidades_especiais;
        this.data_matricula = data_matricula;
        this.RA = RA;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public String getCor_raca() {
        return cor_raca;
    }

    public void setCor_raca(String cor_raca) {
        this.cor_raca = cor_raca;
    }

    public String getDeficiencia() {
        return deficiencia;
    }

    public void setDeficiencia(String deficiencia) {
        this.deficiencia = deficiencia;
    }

    public String getTipo_sanguineo() {
        return tipo_sanguineo;
    }

    public void setTipo_sanguineo(String tipo_sanguineo) {
        this.tipo_sanguineo = tipo_sanguineo;
    }

    public String getNecessidades_especiais() {
        return necessidades_especiais;
    }

    public void setNecessidades_especiais(String necessidades_especiais) {
        this.necessidades_especiais = necessidades_especiais;
    }

    public Date getData_matricula() {
        return data_matricula;
    }

    public void setData_matricula(Date data_matricula) {
        this.data_matricula = data_matricula;
    }

    public String getRA() {
        return RA;
    }

    public void setRA(String RA) {
        this.RA = RA;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlunoMatricula that = (AlunoMatricula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AlunoMatricula{" +
                "id=" + id +
                ", turma=" + turma +
                ", cor_raca='" + cor_raca + '\'' +
                ", deficiencia='" + deficiencia + '\'' +
                ", tipo_sanguineo='" + tipo_sanguineo + '\'' +
                ", necessidades_especiais='" + necessidades_especiais + '\'' +
                ", data_matricula=" + data_matricula +
                ", RA='" + RA + '\'' +
                '}';
    }
}
