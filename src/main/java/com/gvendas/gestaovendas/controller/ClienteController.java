package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.cliente.ClienteRequestDTO;
import com.gvendas.gestaovendas.dto.cliente.ClienteResponseDTO;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Cliente")
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @ApiOperation(value = "Listar", nickname = "findAllCliente")
    @GetMapping
    public List<ClienteResponseDTO> findAll() {
        return clienteService.findAll().stream()
                .map(cliente -> ClienteResponseDTO.convertToClienteDTO(cliente))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por código", nickname = "findByIdCliente")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        return cliente.isPresent() ? ResponseEntity.ok(ClienteResponseDTO.convertToClienteDTO(cliente.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "saveCliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteRequestDTO clienteDto) {
        Cliente clienteSalvo = clienteService.save(clienteDto.convertToEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.convertToClienteDTO(clienteSalvo));
    }

    @ApiOperation(value = "Atualizar", nickname = "updateCliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO clienteDto) {
        Cliente clienteAtualizado = clienteService.update(id, clienteDto.convertToEntity(id));
        return ResponseEntity.ok(ClienteResponseDTO.convertToClienteDTO(clienteAtualizado));
    }

    @ApiOperation(value = "Deletar", nickname = "deleteCliente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }

}
