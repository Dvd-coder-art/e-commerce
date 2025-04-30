package com.project.ecommerce.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private int estoque;
    private Long categoriaId;
    private String categoriaNome;

    public ProdutoResponseDTO() {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
    }


}
