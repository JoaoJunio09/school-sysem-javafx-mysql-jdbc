package br.com.model.dto;

import br.com.model.entities.Aluno;
import br.com.model.entities.AlunoMatricula;

import java.util.Objects;

public class AlunoDTO {

    private Integer id;
    private String nome;
    private String email;
    private String rg;
    private String sexo;
    private String serie;
    private String turma;
    private String RA;

    public AlunoDTO() {}

    public AlunoDTO(Aluno aluno, AlunoMatricula alunoMatricula) {
        this.id = aluno.getId();
        this.nome = aluno.getPessoa().getNome();
        this.email = aluno.getPessoa().getEmail();
        this.rg = aluno.getPessoa().getRg();
        this.sexo = aluno.getPessoa().getSexo();
        this.serie = alunoMatricula.getTurma().getSerie();
        this.turma = alunoMatricula.getTurma().getTurma();
        this.RA = alunoMatricula.getRA();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public void setTurma(String turma) {
        this.turma = turma;
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
        AlunoDTO alunoDTO = (AlunoDTO) o;
        return Objects.equals(id, alunoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AlunoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", rg='" + rg + '\'' +
                ", sexo='" + sexo + '\'' +
                ", serie='" + serie + '\'' +
                ", turma='" + turma + '\'' +
                ", RA='" + RA + '\'' +
                '}';
    }
}
