package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entity.ItemVenda;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.entity.Venda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVendaService {

    protected VendaResponseDTO createVendaResponseDTO(Venda venda, List<ItemVenda> itemVendaList) {
        List<ItemVendaResponseDTO> itensVendasResponseDTO = itemVendaList
                .stream().map(this::createItensVendaResponseDTO).collect(Collectors.toList());
        return new VendaResponseDTO(venda.getCodigo(), venda.getData(), itensVendasResponseDTO);
    }

    protected ItemVendaResponseDTO createItensVendaResponseDTO(ItemVenda itemVenda) {
        return new ItemVendaResponseDTO((itemVenda.getCodigo()), itemVenda.getQuantidade(), itemVenda.getPrecoVendido(),
                itemVenda.getProduto().getCodigo(), itemVenda.getProduto().getDescricao());
    }

    protected ClienteVendaResponseDTO returnClienteVendaResponseDTO(Venda venda, List<ItemVenda> itemVendaList) {
        return new ClienteVendaResponseDTO(venda.getCliente().getNome(),
                Arrays.asList(createVendaResponseDTO(venda, itemVendaList)));
    }

    protected ItemVenda createItemVenda(ItemVendaRequestDTO itemVendaRequestDTO, Venda venda) {
        return new ItemVenda(itemVendaRequestDTO.getQuantidade(), itemVendaRequestDTO.getPrecoVendido(),
                new Produto(itemVendaRequestDTO.getCodigoProduto()), venda);
    }

}
