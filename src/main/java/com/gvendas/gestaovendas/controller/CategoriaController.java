package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.gvendas.gestaovendas.dto.categoria.CategoriaResponseDTO;
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
import java.util.stream.Collectors;

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @ApiOperation(value = "Listar", nickname = "findAllCategoria")
    @GetMapping
    public List<CategoriaResponseDTO> findAll() {
        return categoriaService.findAll().stream()
                .map(categoria -> CategoriaResponseDTO.convertToCategoriaDto(categoria))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por código", nickname = "findByIdCategoria")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findById(id);
        return categoria.isPresent() ? ResponseEntity.ok(CategoriaResponseDTO.convertToCategoriaDto(categoria.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "saveCategoria")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> save(@Valid @RequestBody CategoriaRequestDTO categoriaDto) {
        Categoria categoriaSalva = categoriaService.save(categoriaDto.convertToEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.convertToCategoriaDto(categoriaSalva));
    }

    @ApiOperation(value = "Atualizar", nickname = "updateCategoria")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO categoriaDto) {
        return ResponseEntity.ok(CategoriaResponseDTO.convertToCategoriaDto(categoriaService.update(id, categoriaDto.convertToEntity(id))));
    }

    @ApiOperation(value = "Deletar", nickname = "deleteCategoria")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoriaService.delete(id);
    }

}
