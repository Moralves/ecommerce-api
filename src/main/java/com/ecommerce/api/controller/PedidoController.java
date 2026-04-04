package com.ecommerce.api.controller;

import com.ecommerce.api.dto.PedidoDTO;
import com.ecommerce.api.dto.StatusPedidoDTO;
import com.ecommerce.api.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(pedidoDTO));
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatusPedido(@PathVariable Long id, @Valid @RequestBody StatusPedidoDTO statusDto) {
        return ResponseEntity.ok(pedidoService.atualizarStatusPedido(id, statusDto.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
