package com.project.ecommerce.mapper;

import com.project.ecommerce.dto.CategoriaResponseDTO;
import com.project.ecommerce.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaResponseDTO toDTO(Categoria categoria){
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }

    public static Categoria toEntity(CategoriaResponseDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());  // Não precisa de setId caso esteja criando uma nova categoria
        return categoria;
    }
}
