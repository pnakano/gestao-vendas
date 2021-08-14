package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
