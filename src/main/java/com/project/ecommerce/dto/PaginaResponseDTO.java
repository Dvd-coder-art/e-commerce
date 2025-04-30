package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginaResponseDTO<T> {
    private List<T> conteudo;
    private int paginaAtual;
    private int totalPaginas;
    private long totalElementos;
    private boolean ultima;
    private boolean primeira;

}
