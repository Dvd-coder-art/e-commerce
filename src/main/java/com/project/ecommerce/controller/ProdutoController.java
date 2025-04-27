package com.project.ecommerce.controller;


import com.project.ecommerce.dto.ProdutoDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.entity.ProdutoEntity;
import com.project.ecommerce.mapper.ProdutoMapper;
import com.project.ecommerce.response.ApiResponse;
import com.project.ecommerce.service.CategoriaService;
import com.project.ecommerce.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<ApiResponse<List<ProdutoDTO>>> listarTodos(){
        List<ProdutoDTO> produtos = produtoService.listarTodosProdutos()
                .stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());

        ApiResponse<List<ProdutoDTO>> resposta = new ApiResponse<>(
                true,
                "Produtos listados com sucesso",
                produtos
        );
        return ResponseEntity.ok(resposta);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoDTO>> buscarPorId(@PathVariable Long id){
        return produtoService.buscarPorIdProduto(id)
                .map(produto -> {
                    ProdutoDTO produtoDTO = ProdutoMapper.toDTO(produto);
                    ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                            true,
                            "Produto localizado com sucesso",
                            produtoDTO
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                            false,
                            "Produto não encontrado",
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoDTO>> criar(@RequestBody ProdutoDTO dto){
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (categoria.isEmpty()){
            ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }


        ProdutoEntity entity = ProdutoMapper.toEntity(dto, categoria.get());
        ProdutoEntity salvo = produtoService.salvarProduto(entity);
        ProdutoDTO salvoDTO = ProdutoMapper.toDTO(salvo);
        ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                true,
                "Produto criado com sucesso",
                salvoDTO

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoDTO>> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto){
        Optional<ProdutoEntity> existente = produtoService.buscarPorIdProduto(id);
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (existente.isEmpty()){
            ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                    false,
                    "Produto não encontrado",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (categoria.isEmpty()) {
            ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        ProdutoEntity atualizado = ProdutoMapper.toEntity(dto,categoria.get());
        atualizado.setId(id);
        ProdutoEntity salvo = produtoService.salvarProduto(atualizado);
        ProdutoDTO salvoDTO = ProdutoMapper.toDTO(salvo);

        ApiResponse<ProdutoDTO> response = new ApiResponse<>(
                true,
                "Produto atualizado com sucesso",
                salvoDTO
        );
        return ResponseEntity.ok().body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id){
        Optional<ProdutoEntity> existente = produtoService.buscarPorIdProduto(id);

        if(existente.isEmpty()){
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "Produto não encontrado",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        produtoService.deletarProduto(id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Produto deletado com sucesso",
                null
        );
        return ResponseEntity.ok(response);
    }
}
