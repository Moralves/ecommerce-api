package com.ecommerce.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public void addEndereco(Endereco endereco) {
        enderecos.add(endereco);
        endereco.setCliente(this);
    }

    public void removeEndereco(Endereco endereco) {
        enderecos.remove(endereco);
        endereco.setCliente(null);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
