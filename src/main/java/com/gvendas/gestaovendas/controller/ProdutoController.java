package com.gvendas.gestaovendas.controller;

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

@Api(tags = "Produto")
@RestController
@RequestMapping("/categoria{idCategoria}/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @ApiOperation(value = "Listar todos", nickname = "findAllProduto")
    @GetMapping
    public List<Produto> findAll(@PathVariable Long idCategoria) {
        return produtoService.findAll(idCategoria);
    }

    @ApiOperation(value = "Listar por Id", nickname = "findByIdProduto")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Produto>> findById(@PathVariable Long idCategoria, @PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(idCategoria, id);
        return produto.isPresent() ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "saveProduto")
    @PostMapping
    public ResponseEntity<Produto> save(@PathVariable Long idCategoria, @Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(idCategoria, produto));
    }

    @ApiOperation(value = "Atualizar", nickname = "updateProduto")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable Long idCategoria, @PathVariable Long id, @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.update(idCategoria, id, produto));
    }

    @ApiOperation(value = "Deletar", nickname = "deleteProduto")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategoria, @PathVariable Long id) {
        produtoService.delete(idCategoria, id);
    }

}
