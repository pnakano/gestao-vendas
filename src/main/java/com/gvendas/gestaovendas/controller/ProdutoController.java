package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.produto.ProdutoRequestDTO;
import com.gvendas.gestaovendas.dto.produto.ProdutoResponseDTO;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.service.ProdutoService;
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

@Api(tags = "Produto")
@RestController
@RequestMapping("/categoria{idCategoria}/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @ApiOperation(value = "Listar todos", nickname = "findAllProduto")
    @GetMapping
    public List<ProdutoResponseDTO> findAll(@PathVariable Long idCategoria) {
        return produtoService.findAll(idCategoria).stream()
                .map(produto -> ProdutoResponseDTO.convertToProdutoDTO(produto))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por Id", nickname = "findByIdProduto")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long idCategoria, @PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(idCategoria, id);
        return produto.isPresent() ? ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produto.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "saveProduto")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> save(@PathVariable Long idCategoria, @Valid @RequestBody ProdutoRequestDTO produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponseDTO.convertToProdutoDTO(produtoService.save(idCategoria, produto.convertToEntity(idCategoria))));
    }

    @ApiOperation(value = "Atualizar", nickname = "updateProduto")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long idCategoria, @PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO produto) {
        return ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produtoService.update(idCategoria, id, produto.convertToEntity(idCategoria, id))));
    }

    @ApiOperation(value = "Deletar", nickname = "deleteProduto")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategoria, @PathVariable Long id) {
        produtoService.delete(idCategoria, id);
    }

}
