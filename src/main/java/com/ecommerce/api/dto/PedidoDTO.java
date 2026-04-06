package com.ecommerce.api.dto;

import com.ecommerce.api.entity.StatusPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {
    private Long id;

    private LocalDateTime data;
    private StatusPedido status;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID do endereço é obrigatório")
    private Long enderecoEntregaId;

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemPedidoDTO> itens = new ArrayList<>();

    private BigDecimal total;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, LocalDateTime data, StatusPedido status, Long clienteId, Long enderecoEntregaId, List<ItemPedidoDTO> itens, BigDecimal total) {
        this.id = id;
        this.data = data;
        this.status = status;
        this.clienteId = clienteId;
        this.enderecoEntregaId = enderecoEntregaId;
        this.itens = itens;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getEnderecoEntregaId() {
        return enderecoEntregaId;
    }

    public void setEnderecoEntregaId(Long enderecoEntregaId) {
        this.enderecoEntregaId = enderecoEntregaId;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
