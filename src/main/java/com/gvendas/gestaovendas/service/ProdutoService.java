package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaService categoriaService;

    public List<Produto> findAll(Long idCategoria){
        return produtoRepository.findByCategoriaCodigo(idCategoria);
    }

    public Optional<Produto> findById(Long idCategoria, Long idProduto){
        return  produtoRepository.findById(idCategoria,idProduto);
    }

    public  Produto save(Long idCategoria, Produto produto){
        validarCategoriaDoProdutoExiste(idCategoria);
        validarProdutoDuplicado(produto);
        return produtoRepository.save(produto);
    }

    public Produto update(Long idCategoria, Long idProduto, Produto produto){
        Produto produtoSalvar = validaProdutoExiste(idCategoria, idProduto);
        validarCategoriaDoProdutoExiste(idCategoria);
        validarProdutoDuplicado(produto);
        BeanUtils.copyProperties(produto, produtoSalvar, "id");
        return produtoRepository.save(produtoSalvar);
    }

    public void delete(Long idCategoria, Long idProduto){
        Produto produto = validaProdutoExiste(idCategoria, idProduto);
        produtoRepository.delete(produto);
    }

    private Produto validaProdutoExiste(Long idCategoria, Long idProduto){
        Optional<Produto> produto = findById(idCategoria, idProduto);
        if (produto.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return produto.get();
    }

    private void validarProdutoDuplicado(Produto produto){
        Optional<Produto> produtoPorDescricao = produtoRepository.findByDescricaoAndCategoriaCodigo(produto.getDescricao(),produto.getCategoria().getCodigo());
        if(produtoPorDescricao.isPresent() && produtoPorDescricao.get().getCodigo() != produto.getCodigo()){
            throw new RegraNegocioException(String.format("O produto %s já está cadastrado para esta categoria", produto.getDescricao()));
        }
    }

    private void validarCategoriaDoProdutoExiste(Long idCategoria){
        if(idCategoria == null){
            throw new RegraNegocioException("A categoria não pode ser nula");
        }
        if (categoriaService.findById(idCategoria).isEmpty()){
            throw new RegraNegocioException(String.format("A categoria de código %d não existe no cadastro", idCategoria));
        }
    }
}
