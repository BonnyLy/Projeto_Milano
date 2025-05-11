package com.milano.sistema.milano.model;

// import com.milano.model.enums.CategoriaProduto;
import com.milano.sistema.milano.model.enums.CategoriaProduto;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProduto categoria;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (categoria == null) {
            categoria = CategoriaProduto.OUTROS;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}