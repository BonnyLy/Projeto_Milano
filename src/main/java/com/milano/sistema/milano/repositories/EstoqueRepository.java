package com.milano.sistema.milano.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milano.sistema.milano.model.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long produtoId);
}