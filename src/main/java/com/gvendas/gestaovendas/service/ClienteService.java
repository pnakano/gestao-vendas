package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        Cliente clienteSalvar = validaProdutoExiste(id);
        BeanUtils.copyProperties(cliente, clienteSalvar, "id");
        return clienteRepository.save(cliente);
    }

    private Cliente validaProdutoExiste(Long id) {
        Optional<Cliente> cliente = findById(id);
        if (cliente.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return cliente.get();
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
