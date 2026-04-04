package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;

public class EnderecoDTO {
    private Long id;

    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    public EnderecoDTO() {
    }

    public EnderecoDTO(Long id, String rua, String cidade, String cep) {
        this.id = id;
        this.rua = rua;
        this.cidade = cidade;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
