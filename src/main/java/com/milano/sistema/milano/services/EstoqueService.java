package com.milano.sistema.milano.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.milano.sistema.milano.model.Estoque;
import com.milano.sistema.milano.model.Produto;
import com.milano.sistema.milano.repositories.EstoqueRepository;
import com.milano.sistema.milano.repositories.ProdutoRepository;

import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Estoque criarEntradaEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Optional<Estoque> estoqueExistente = estoqueRepository.findByProdutoId(produtoId);
        
        if (estoqueExistente.isPresent()) {
            Estoque estoque = estoqueExistente.get();
            estoque.setQuantidade(estoque.getQuantidade() + quantidade);
            return estoqueRepository.save(estoque);
        } else {
            Estoque novoEstoque = new Estoque();
            novoEstoque.setProduto(produto);
            novoEstoque.setQuantidade(quantidade);
            return estoqueRepository.save(novoEstoque);
        }
    }

    public Optional<Estoque> consultarEstoquePorProduto(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId);
    }

    public Estoque atualizarEstoque(Long produtoId, Integer novaQuantidade) {
        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto"));
        
        estoque.setQuantidade(novaQuantidade);
        return estoqueRepository.save(estoque);
    }
}