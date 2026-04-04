package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ItemPedidoDTO {
    private Long id;

    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    private BigDecimal precoUnitario;

    public ItemPedidoDTO() {
    }

    public ItemPedidoDTO(Long id, Long produtoId, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
