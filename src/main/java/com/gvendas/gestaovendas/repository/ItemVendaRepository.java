package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Query("""
            SELECT new com.gvendas.gestaovendas.entity.ItemVenda(
                IV.codigo, IV.quantidade, IV.precoVendido, IV.produto, IV.venda)
              FROM ItemVenda IV
             WHERE IV.venda.codigo = :idVenda
            """)
    List<ItemVenda> findByVendaCodigo(Long idVenda);

}
