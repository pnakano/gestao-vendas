package com.gvendas.gestaovendas.dto.cliente;

import com.gvendas.gestaovendas.dto.endereco.EnderecoRequestDTO;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.entity.Endereco;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel("Cliente Requisição DTO")
public class ClienteRequestDTO {

    @ApiModelProperty(value = "Nome")
    @NotBlank(message = "Nome")
    @Length(min = 3, max = 50, message = "Nome")
    private String nome;

    @ApiModelProperty(value = "Telefone")
    @NotBlank(message = "Telefone")
    @Pattern(regexp = "\\([\\d]{2}\\)[\\d]{5}[- .][\\d]{4}", message = "Telefone")
    private String telefone;

    @ApiModelProperty(value = "Ativo")
    @NotNull(message = "Ativo")
    private Boolean ativo;

    @ApiModelProperty(value = "Endereço")
    @NotNull(message = "Endereço")
    @Valid
    private EnderecoRequestDTO enderecoRequestDTO;

    public ClienteRequestDTO(String nome, String telefone, Boolean ativo, EnderecoRequestDTO enderecoRequestDTO) {
        this.nome = nome;
        this.telefone = telefone;
        this.ativo = ativo;
        this.enderecoRequestDTO = enderecoRequestDTO;
    }

    public Cliente convertToEntity() {
        Endereco endereco = new Endereco(enderecoRequestDTO.getLogradouro(), enderecoRequestDTO.getNumero(), enderecoRequestDTO.getComplemento(), enderecoRequestDTO.getBairro(),
                enderecoRequestDTO.getCep(), enderecoRequestDTO.getCidade(), enderecoRequestDTO.getEstado());
        return new Cliente(nome, telefone, ativo, endereco);
    }

    public Cliente convertToEntity(Long codigo) {
        Endereco endereco = new Endereco(enderecoRequestDTO.getLogradouro(), enderecoRequestDTO.getNumero(), enderecoRequestDTO.getComplemento(), enderecoRequestDTO.getBairro(),
                enderecoRequestDTO.getCep(), enderecoRequestDTO.getCidade(), enderecoRequestDTO.getEstado());
        return new Cliente(codigo, nome, telefone, ativo, endereco);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnderecoRequestDTO getEnderecoRequestDTO() {
        return enderecoRequestDTO;
    }

    public void setEnderecoRequestDTO(EnderecoRequestDTO enderecoRequestDTO) {
        this.enderecoRequestDTO = enderecoRequestDTO;
    }
}
