package com.gvendas.gestaovendas.dto.categoria;

import com.gvendas.gestaovendas.entity.Categoria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel("Categoria Requisição DTO")
public class CategoriaRequestDTO {

    @ApiModelProperty(value = "Nome")
    @NotBlank(message = "Nome")
    @Length(min = 3, max = 50, message = "Nome")
    private String nome;

    public Categoria convertToEntity() {
        return new Categoria(nome);
    }

    public Categoria convertToEntity(Long id) {
        return new Categoria(id, nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
