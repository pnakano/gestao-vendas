package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.VendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.entity.ItemVenda;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.entity.Venda;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.ItemVendaRepository;
import com.gvendas.gestaovendas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService extends AbstractVendaService {

    private VendaRepository vendaRepository;
    private ItemVendaRepository itemVendaRepository;
    private ClienteService clienteService;
    private ProdutoService produtoService;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ClienteService clienteService, ItemVendaRepository itemVendaRepository, ProdutoService produtoService) {
        this.vendaRepository = vendaRepository;
        this.clienteService = clienteService;
        this.itemVendaRepository = itemVendaRepository;
        this.produtoService = produtoService;
    }

    public ClienteVendaResponseDTO findVendaByCliente(Long idCliente) {
        Cliente cliente = validarClienteVendaExiste(idCliente);
        List<VendaResponseDTO> vendaResponseDTOList = vendaRepository.findByClienteCodigo(idCliente).stream()
                .map(venda -> createVendaResponseDTO(venda, itemVendaRepository.findByVendaCodigo(venda.getCodigo())))
                .collect(Collectors.toList());
        return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDTOList);
    }

    public ClienteVendaResponseDTO findVendaById(Long idVenda) {
        Venda venda = validarVendaExiste(idVenda);
        List<ItemVenda> itensVendaList = itemVendaRepository.findByVendaCodigo(venda.getCodigo());
        return returnClienteVendaResponseDTO(venda, itensVendaList);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public ClienteVendaResponseDTO save(Long idCliente, VendaRequestDTO vendaRequestDTO) {
        Cliente cliente = validarClienteVendaExiste(idCliente);
        validarProdutoExisteEAtualizarQuantidade(vendaRequestDTO.getItensVendaDto());
        Venda vendaSalva = saveVenda(cliente, vendaRequestDTO);
        return returnClienteVendaResponseDTO(vendaSalva, itemVendaRepository.findByVendaCodigo(vendaSalva.getCodigo()));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void delete(Long idVenda) {
        validarVendaExiste(idVenda);
        List<ItemVenda> itemVendaList = itemVendaRepository.findByVendaCodigo(idVenda);
        validaProdutoExisteEDevolveQuantidade(itemVendaList);
        itemVendaRepository.deleteAll(itemVendaList);
        vendaRepository.deleteById(idVenda);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public ClienteVendaResponseDTO update(Long idVenda, Long idCliente, VendaRequestDTO vendaRequestDTO) {
        validarVendaExiste(idVenda);
        Cliente cliente = validarClienteVendaExiste(idCliente);
        List<ItemVenda> itemVendaList = itemVendaRepository.findByVendaCodigo(idVenda);
        validaProdutoExisteEDevolveQuantidade(itemVendaList);
        validarProdutoExisteEAtualizarQuantidade(vendaRequestDTO.getItensVendaDto());
        itemVendaRepository.deleteAll(itemVendaList);
        Venda vendaAtualizada = atualizarVenda(idVenda, cliente, vendaRequestDTO);
        return returnClienteVendaResponseDTO(vendaAtualizada, itemVendaRepository.findByVendaCodigo(idVenda));
    }

    private void validaProdutoExisteEDevolveQuantidade(List<ItemVenda> itemVendaList) {
        itemVendaList.forEach(item -> {
            Produto produto = produtoService.validaProdutoExiste(item.getProduto().getCodigo());
            produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            produtoService.atualizarQuantidade(produto);
        });
    }

    private Venda saveVenda(Cliente cliente, VendaRequestDTO vendaRequestDTO) {
        Venda vendaSalva = vendaRepository.save(new Venda(vendaRequestDTO.getData(), cliente));
        vendaRequestDTO.getItensVendaDto().stream()
                .map(itemVendaRequestDTO -> createItemVenda(itemVendaRequestDTO, vendaSalva))
                .forEach(itemVendaRepository::save);
        return vendaSalva;
    }

    private Venda atualizarVenda(Long idVenda, Cliente cliente, VendaRequestDTO vendaRequestDTO) {
        Venda vendaSalva = vendaRepository.save(new Venda(idVenda, vendaRequestDTO.getData(), cliente));
        vendaRequestDTO.getItensVendaDto().stream()
                .map(itemVendaRequestDTO -> createItemVenda(itemVendaRequestDTO, vendaSalva))
                .forEach(itemVendaRepository::save);
        return vendaSalva;
    }

    private void validarProdutoExisteEAtualizarQuantidade(List<ItemVendaRequestDTO> itensVendaDto) {
        itensVendaDto.forEach(item -> {
            Produto produto = produtoService.validaProdutoExiste(item.getCodigoProduto());
            validarQuantidadeProdutoExiste(produto, item.getQuantidade());
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoService.atualizarQuantidade(produto);
        });
    }

    private void validarQuantidadeProdutoExiste(Produto produto, Integer qtdeVendaDto) {
        if (produto.getQuantidade() < qtdeVendaDto) {
            throw new RegraNegocioException(String.format("A quantidade %s informada para o produto %s não está disponível em estoque.",
                    qtdeVendaDto, produto.getDescricao()));
        }
    }

    private Venda validarVendaExiste(Long idVenda) {
        Optional<Venda> venda = vendaRepository.findById(idVenda);
        if (venda.isEmpty()) {
            throw new RegraNegocioException(String.format("Venda de código %s não encontrado.", idVenda));
        }
        return venda.get();
    }

    private Cliente validarClienteVendaExiste(Long idCLiente) {
        Optional<Cliente> cliente = clienteService.findById(idCLiente);
        if (cliente.isEmpty()) {
            throw new RegraNegocioException(String.format("O Cliente de código %s não está cadastrado", idCLiente));
        }
        return cliente.get();
    }

}
