package com.ecommerce.api.service;

import com.ecommerce.api.dto.ClienteDTO;
import com.ecommerce.api.dto.EnderecoDTO;
import com.ecommerce.api.exception.RecursoNaoEncontradoException;
import com.ecommerce.api.exception.RegraNegocioException;
import com.ecommerce.api.entity.Cliente;
import com.ecommerce.api.entity.Endereco;
import com.ecommerce.api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteDTO criarCliente(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado no sistema.");
        }

        Cliente cliente = new Cliente(null, dto.getNome(), dto.getEmail());

        if (dto.getEnderecos() != null) {
            for (EnderecoDTO endDto : dto.getEnderecos()) {
                Endereco endereco = new Endereco(null, endDto.getRua(), endDto.getCidade(), endDto.getCep(), cliente);
                cliente.addEndereco(endereco);
            }
        }

        cliente = clienteRepository.save(cliente);
        return converterParaDTO(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public ClienteDTO atualizarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));

        if (!cliente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado no sistema para outro cliente.");
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());

        cliente = clienteRepository.save(cliente);
        return converterParaDTO(cliente);
    }

    @Transactional
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
             throw new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO converterParaDTO(Cliente cliente) {
        List<EnderecoDTO> enderecosDTO = cliente.getEnderecos().stream()
                .map(e -> new EnderecoDTO(e.getId(), e.getRua(), e.getCidade(), e.getCep()))
                .collect(Collectors.toList());
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), enderecosDTO);
    }
}
