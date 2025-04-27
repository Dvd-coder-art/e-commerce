package com.project.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaResponseDTO {

    private Long id;
    private String nome;



    public CategoriaResponseDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}