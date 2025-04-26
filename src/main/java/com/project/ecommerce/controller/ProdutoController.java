package com.project.ecommerce.controller;


import com.project.ecommerce.dto.ProdutoDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.entity.ProdutoEntity;
import com.project.ecommerce.mapper.ProdutoMapper;
import com.project.ecommerce.service.CategoriaService;
import com.project.ecommerce.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;


    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService){
        this.produtoService= produtoService;
        this.categoriaService = categoriaService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<ProdutoDTO> listarTodos(){
        return produtoService.listarTodosProdutos()
                .stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id){
        return produtoService.buscarPorIdProduto(id)
                .map(ProdutoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO dto){
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (categoria.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        ProdutoEntity entity = ProdutoMapper.toEntity(dto, categoria.get());
        ProdutoEntity salvo = produtoService.salvarProduto(entity);
        return ResponseEntity.ok(ProdutoMapper.toDTO(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto){
        Optional<ProdutoEntity> existente = produtoService.buscarPorIdProduto(id);
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (existente.isEmpty()|| categoria.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        ProdutoEntity atualizado = ProdutoMapper.toEntity(dto,categoria.get());
        atualizado.setId(id);
        ProdutoEntity salvo = produtoService.salvarProduto(atualizado);
        return ResponseEntity.ok(ProdutoMapper.toDTO(salvo));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if(produtoService.buscarPorIdProduto(id).isPresent()){
            produtoService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
