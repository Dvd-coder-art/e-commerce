package com.project.ecommerce.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    private String nome;

    @Version
    private Integer versao;

    @OneToMany(mappedBy = "categoria")
    private List<ProdutoEntity> produtos;
}
