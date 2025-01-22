package br.com.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String serie;
    private String turma;

    public Turma() {
    }

    public Turma(Integer id, String serie, String turma) {
        this.id = id;
        this.serie = serie;
        this.turma = turma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String descricao) {
        this.turma = descricao;
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
        return serie + " " + turma;
    }
}