package com.milano.sistema.milano.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.milano.sistema.milano.model.Produto;
import com.milano.sistema.milano.repositories.ProdutoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto criarProduto(Produto produto) {
        produto.setDataCriacao(LocalDateTime.now());
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutosAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoAtualizado.getNome());
                    produto.setDescricao(produtoAtualizado.getDescricao());
                    produto.setPreco(produtoAtualizado.getPreco());
                    return produtoRepository.save(produto);
                })
                .orElseGet(() -> {
                    produtoAtualizado.setId(id);
                    return produtoRepository.save(produtoAtualizado);
                });
    }

    public void desativarProduto(Long id) {
        produtoRepository.desativarProduto(id);
    }
}