package com.milano.sistema.milano.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milano.sistema.milano.model.Estoque;
import com.milano.sistema.milano.services.EstoqueService;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/{produtoId}")
    public ResponseEntity<Estoque> adicionarEstoque(
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        Estoque estoque = estoqueService.criarEntradaEstoque(produtoId, quantidade);
        return ResponseEntity.ok(estoque);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<Estoque> consultarEstoque(@PathVariable Long produtoId) {
        return estoqueService.consultarEstoquePorProduto(produtoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<Estoque> atualizarEstoque(
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        Estoque estoque = estoqueService.atualizarEstoque(produtoId, quantidade);
        return ResponseEntity.ok(estoque);
    }
}