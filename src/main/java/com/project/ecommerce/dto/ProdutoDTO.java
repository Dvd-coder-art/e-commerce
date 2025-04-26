package com.project.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private int estoque;
    private Long categoriaId;
    private String categoriaNome;
}
