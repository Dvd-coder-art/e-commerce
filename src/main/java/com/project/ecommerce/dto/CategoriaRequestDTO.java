package com.project.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;
}
