package com.project.ecommerce.mapper;

import com.project.ecommerce.dto.ProdutoDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.entity.ProdutoEntity;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(ProdutoEntity produto){
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        dto.setEstoque(produto.getEstoque());

        if (produto.getCategoria() != null){
            dto.setCategoriaId(produto.getCategoria().getId());
            dto.setCategoriaNome(produto.getCategoria().getNome());
        }
        return dto;
    }

    public static ProdutoEntity toEntity(ProdutoDTO dto, Categoria categoria){
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setCategoria(categoria);
        return produto;
    }
}
