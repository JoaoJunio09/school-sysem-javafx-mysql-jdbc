package br.com.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Pessoa pessoa;
    private String filiacao;
    private String n_carteira_trab;
    private String n_ident_pis;
    private Date data_emissao;
    private Date data_demissao;

    public Funcionario() {}

    public Funcionario(Integer id, Pessoa pessoa, String filiacao, String n_carteira_trab,
    String n_ident_pis, Date data_emissao, Date data_demissao
    ) {
        this.id = id;
        this.pessoa = pessoa;
        this.filiacao = filiacao;
        this.n_carteira_trab = n_carteira_trab;
        this.n_ident_pis = n_ident_pis;
        this.data_emissao = data_emissao;
        this.data_demissao = data_demissao;
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

    public String getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
    }

    public String getN_carteira_trab() {
        return n_carteira_trab;
    }

    public void setN_carteira_trab(String n_carteira_trab) {
        this.n_carteira_trab = n_carteira_trab;
    }

    public String getN_ident_pis() {
        return n_ident_pis;
    }

    public void setN_ident_pis(String n_ident_pis) {
        this.n_ident_pis = n_ident_pis;
    }

    public Date getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(Date data_emissao) {
        this.data_emissao = data_emissao;
    }

    public Date getData_demissao() {
        return data_demissao;
    }

    public void setData_demissao(Date data_demissao) {
        this.data_demissao = data_demissao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                ", filiacao='" + filiacao + '\'' +
                ", n_carteira_trab='" + n_carteira_trab + '\'' +
                ", n_ident_pis='" + n_ident_pis + '\'' +
                ", data_emissao=" + data_emissao +
                ", data_demissao=" + data_demissao +
                '}';
    }
}
