package com.ecommerce.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class ClienteDTO {
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    private List<EnderecoDTO> enderecos = new ArrayList<>();

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nome, String email, List<EnderecoDTO> enderecos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.enderecos = enderecos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }
}
