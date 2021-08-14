package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.service.CategoriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categoria")  //define o caminho de acesso
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @ApiOperation(value = "Listar", nickname = "findAllCategoria")
    @GetMapping   //"É uma consulta"
    public List<Categoria> findAll(){
        return  categoriaService.findAll();
    }

    @ApiOperation(value = "Listar por código", nickname = "findByIdCategoria")
    @GetMapping("/{id}")  //quando tem mais de um getmapping, precisa diferenciar pra não se perder nos caminhos
    public ResponseEntity<Optional<Categoria>> findById(@PathVariable Long id){
           //ResponseEntity para tratar pelo retorno
        Optional<Categoria> categoria = categoriaService.findById(id);
        return categoria.isPresent() ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "saveCategoria")
    @PostMapping
    public ResponseEntity<Categoria> save(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoria));

//        Forma comprida de fazer:
//        Categoria categoriaSalva = categoriaServico.salvar(categoria);
//        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @ApiOperation(value = "Atualizar", nickname = "updateCategoria")
    @PutMapping("/{id}")                       //Variavel             //Entidade a atualizar
    public ResponseEntity<Categoria> update(@PathVariable Long id, @Valid @RequestBody Categoria categoria){
        return ResponseEntity.ok(categoriaService.update(id, categoria));
    }

    @ApiOperation(value = "Deletar", nickname = "deleteCategoria")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoriaService.delete(id);
    }

}
