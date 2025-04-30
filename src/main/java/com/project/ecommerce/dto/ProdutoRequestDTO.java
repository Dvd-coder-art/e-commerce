package com.project.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class ProdutoRequestDTO {
    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O preço não pode ser nulo.")
    private BigDecimal preco;

    @Min(value = 0, message = "O estoque não pode ser negativo.")
    private int estoque;

    @NotNull(message = "O ID da categoria não pode ser nulo.")
    private Long categoriaId;

    private String categoriaNome;
}
