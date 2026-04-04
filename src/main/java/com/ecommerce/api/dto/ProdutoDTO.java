package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ProdutoDTO {
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser maior ou igual a zero")
    private BigDecimal preco;

    @NotNull(message = "Estoque é obrigatório")
    @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero")
    private Integer estoque;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String nome, BigDecimal preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
