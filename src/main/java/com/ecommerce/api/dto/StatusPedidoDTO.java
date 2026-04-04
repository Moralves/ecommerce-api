package com.ecommerce.api.dto;

import com.ecommerce.api.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public class StatusPedidoDTO {
    
    @NotNull(message = "O status não pode ser nulo")
    private StatusPedido status;
    
    public StatusPedidoDTO() {
    }
    
    public StatusPedidoDTO(StatusPedido status) {
        this.status = status;
    }
    
    public StatusPedido getStatus() {
        return status;
    }
    
    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}
