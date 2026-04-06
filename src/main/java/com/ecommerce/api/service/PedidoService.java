package com.ecommerce.api.service;

import com.ecommerce.api.dto.ItemPedidoDTO;
import com.ecommerce.api.dto.PedidoDTO;
import com.ecommerce.api.exception.RecursoNaoEncontradoException;
import com.ecommerce.api.exception.RegraNegocioException;
import com.ecommerce.api.entity.*;
import com.ecommerce.api.repository.ClienteRepository;
import com.ecommerce.api.repository.EnderecoRepository;
import com.ecommerce.api.repository.PedidoRepository;
import com.ecommerce.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoDTO criarPedido(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        Endereco endereco = enderecoRepository.findById(dto.getEnderecoEntregaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço de entrega não encontrado"));

        Pedido pedido = new Pedido(null, LocalDateTime.now(), StatusPedido.CRIADO, cliente, endereco);
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: ID " + itemDto.getProdutoId()));

            if (produto.getEstoque() < itemDto.getQuantidade()) {
                throw new RegraNegocioException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido(null, itemDto.getQuantidade(), produto.getPreco(), pedido, produto);
            pedido.addItem(item);

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            totalPedido = totalPedido.add(subtotal);
        }

        pedido.setTotal(totalPedido);
        pedido = pedidoRepository.save(pedido);

        return converterParaDTO(pedido);
    }

    public List<PedidoDTO> listarPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    @Transactional
    public PedidoDTO atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Não é possível alterar status de um pedido já cancelado");
        }

        // Fluxo de status: CRIADO -> PAGO -> ENVIADO
        if (pedido.getStatus() == StatusPedido.CRIADO && novoStatus != StatusPedido.PAGO) {
             throw new RegraNegocioException("Um pedido CRIADO só pode ser alterado para PAGO");
        }
        
        if (pedido.getStatus() == StatusPedido.PAGO && novoStatus != StatusPedido.ENVIADO) {
             throw new RegraNegocioException("Um pedido PAGO só pode ser alterado para ENVIADO");
        }
        
        if (pedido.getStatus() == StatusPedido.ENVIADO) {
             throw new RegraNegocioException("Pedido já foi ENVIADO e não pode ter status alterado");
        }

        pedido.setStatus(novoStatus);
        pedido = pedidoRepository.save(pedido);

        return converterParaDTO(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new RegraNegocioException("Apenas pedidos com status CRIADO podem ser cancelados");
        }

        // Devolve o estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private PedidoDTO converterParaDTO(Pedido pedido) {
        List<ItemPedidoDTO> itensDTO = pedido.getItens().stream()
                .map(i -> new ItemPedidoDTO(i.getId(), i.getProduto().getId(), i.getQuantidade(), i.getPrecoUnitario()))
                .collect(Collectors.toList());

        return new PedidoDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getCliente().getId(),
                pedido.getEnderecoEntrega().getId(),
                itensDTO,
                pedido.getTotal()
        );
    }
}
