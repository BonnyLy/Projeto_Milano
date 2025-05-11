package com.milano.sistema.milano.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.milano.sistema.milano.model.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();
    
    @Modifying
    @Query("UPDATE Produto p SET p.ativo = false WHERE p.id = ?1")
    void desativarProduto(Long id);
}