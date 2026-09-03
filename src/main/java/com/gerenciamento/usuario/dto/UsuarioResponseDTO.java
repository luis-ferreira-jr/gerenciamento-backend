package com.gerenciamento.usuario.dto;

import com.gerenciamento.usuario.Usuario;
import java.time.LocalDateTime;

/**
 * Formato exposto pela API. Nunca inclui a senha.
 */
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private LocalDateTime dataCriacao;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCpf();
        this.dataCriacao = usuario.getDataCriacao();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
