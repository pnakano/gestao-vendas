package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaRequestDTO;
import com.gvendas.gestaovendas.service.VendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Venda")
@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @ApiOperation(value = "Listar Vendas por Cliente", nickname = "findVendaByCliente")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> findVendaByCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(vendaService.findVendaByCliente(idCliente));
    }

    @ApiOperation(value = "Listar Vendas pelo Código", nickname = "findVendaById")
    @GetMapping("/{idVenda}")
    public ResponseEntity<ClienteVendaResponseDTO> findVendaById(@PathVariable Long idVenda) {
        return ResponseEntity.ok(vendaService.findVendaById(idVenda));
    }

    @ApiOperation(value = "Salvar Venda", nickname = "saveVenda")
    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> save(@PathVariable Long idCliente, @Valid @RequestBody VendaRequestDTO vendaRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.save(idCliente, vendaRequestDTO));
    }

    @ApiOperation(value = "Deletar Venda", nickname = "deleteVenda")
    @DeleteMapping("/{idVenda}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idVenda) {
        vendaService.delete(idVenda);
    }

    @ApiOperation(value = "Atualizar Venda", nickname = "updateVenda")
    @PutMapping("{idVenda}/cliente/{idCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> update(@PathVariable Long idVenda, @PathVariable Long idCliente, @Valid @RequestBody VendaRequestDTO vendaRequestDTO) {
        return ResponseEntity.ok(vendaService.update(idVenda, idCliente, vendaRequestDTO));
    }
}


