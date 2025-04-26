package com.project.ecommerce.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class ProdutoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    private String nome;
    private BigDecimal preco;
    private int estoque;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
