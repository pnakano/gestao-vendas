package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaCodigo(Long idCategoria);

    @Query("""
            SELECT PROD
              FROM Produto PROD
             WHERE PROD.codigo = :idProduto
               AND PROD.categoria.codigo = :idCategoria""")
    Optional<Produto> findById(Long idCategoria, Long idProduto);
    //Se criar o método como findByCodigoAndCategoriaCodigo não precisaria da query, só forçando um exemplo usando @Query

    Optional<Produto> findByDescricaoAndCategoriaCodigo(String descricao, Long idCategoria);
}
