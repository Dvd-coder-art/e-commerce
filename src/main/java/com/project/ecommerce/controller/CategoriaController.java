package com.project.ecommerce.controller;


import com.project.ecommerce.dto.CategoriaResponseDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.mapper.CategoriaMapper;
import com.project.ecommerce.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService=categoriaService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CategoriaResponseDTO> listarTodos(){
        return categoriaService.listarCategoria()
                .stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> listarPorId(@PathVariable Long id){
        return categoriaService.listarPorId(id)
                .map(CategoriaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaResponseDTO dto){
        Categoria salvo = categoriaService.salvarCategoria(CategoriaMapper.toEntity(dto));
        return ResponseEntity.ok(CategoriaMapper.toDTO(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaResponseDTO dto){
        Optional<Categoria> existente = categoriaService.listarPorId(id);

        if (existente.isEmpty()){
            ResponseEntity.notFound().build();
        }
        Categoria atualizado = CategoriaMapper.toEntity(dto);
        atualizado.setId(id);
        Categoria salvo = categoriaService.salvarCategoria(atualizado);


       return ResponseEntity.ok(CategoriaMapper.toDTO(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if (categoriaService.listarPorId(id).isPresent()){
            categoriaService.deletarCategoria(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
