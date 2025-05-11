package com.milano.sistema.milano.model;

import com.milano.sistema.milano.model.enums.StatusEstoque;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estoque")
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEstoque status;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
        atualizarStatus();
    }

    private void atualizarStatus() {
        if (quantidade > 10) {
            status = StatusEstoque.DISPONIVEL;
        } else if (quantidade > 0) {
            status = StatusEstoque.ULTIMAS_UNIDADES;
        } else {
            status = StatusEstoque.ESGOTADO;
        }
    }
}




