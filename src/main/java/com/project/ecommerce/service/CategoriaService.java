package com.project.ecommerce.service;

import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria>listarCategoria(){
        return categoriaRepository.findAll();
    }

    public Optional<Categoria>listarPorId(Long id){
        return categoriaRepository.findById(id);
    }

    public Categoria salvarCategoria(Categoria produto){
        return categoriaRepository.save(produto);
    }

    public void deletarCategoria(Long id){
        categoriaRepository.deleteById(id);
    }
}
