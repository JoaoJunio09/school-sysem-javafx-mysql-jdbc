package br.com.model.entities;

import br.com.model.enums.TipoUsuarioEnum;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String senha;
    private TipoUsuarioEnum tipoUsuario;

    public Usuario() {}
}
