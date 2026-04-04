package com.ecommerce.api.service;

import com.ecommerce.api.dto.ProdutoDTO;
import com.ecommerce.api.exception.RecursoNaoEncontradoException;
import com.ecommerce.api.model.Produto;
import com.ecommerce.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoDTO criarProduto(ProdutoDTO dto) {
        Produto produto = new Produto(null, dto.getNome(), dto.getPreco(), dto.getEstoque());
        produto = produtoRepository.save(produto);
        return converterParaDTO(produto);
    }

    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id));

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());

        produto = produtoRepository.save(produto);
        return converterParaDTO(produto);
    }

    @Transactional
    public void deletarProduto(Long id) {
         if (!produtoRepository.existsById(id)) {
             throw new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id);
         }
         produtoRepository.deleteById(id);
    }

    private ProdutoDTO converterParaDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getEstoque());
    }
}
