package com.project.ecommerce.controller;


import com.project.ecommerce.dto.CategoriaResponseDTO;
import com.project.ecommerce.dto.ProdutoDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.mapper.CategoriaMapper;
import com.project.ecommerce.response.ApiResponse;
import com.project.ecommerce.service.CategoriaService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<CategoriaResponseDTO>>> listarTodos(){
        List<CategoriaResponseDTO> categoria = categoriaService.listarCategoria()
                .stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());

        ApiResponse<List<CategoriaResponseDTO>> response = new ApiResponse<>(
                true,
                "Categorias listados com sucesso",
                categoria
        );

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> listarPorId(@PathVariable Long id){

        return categoriaService.listarPorId(id)
                .map(categoria -> {
                    CategoriaResponseDTO categoriaDTO = CategoriaMapper.toDTO(categoria);
                    ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                            true,
                            "Categoria encontrada",
                            categoriaDTO
                    );
                    return ResponseEntity.ok(response);
                }).orElseGet(() ->{
                    ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                            false,
                            "Categoria não encontrada",
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> criar(@RequestBody CategoriaResponseDTO dto){
        Categoria salvo = categoriaService.salvarCategoria(CategoriaMapper.toEntity(dto));
        CategoriaResponseDTO categoriaSave = CategoriaMapper.toDTO(salvo);
        ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                true,
                "Categoria salva com sucesso",
                categoriaSave
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> atualizar(@PathVariable Long id, @RequestBody CategoriaResponseDTO dto){

        Optional<Categoria> existenteOpt = categoriaService.listarPorId(id);

        if (existenteOpt.isEmpty()){
            ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Categoria existente = existenteOpt.get();
        existente.setNome(dto.getNome());


        Categoria salvo = categoriaService.salvarCategoria(existente);
        CategoriaResponseDTO categoriaSave = CategoriaMapper.toDTO(salvo);

        ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                true,
                "Categoria atualizada com sucesso",
                categoriaSave
        );


       return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id){
        Optional<Categoria> categoria = categoriaService.listarPorId(id);
        if (categoria.isEmpty()){
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        categoriaService.deletarCategoria(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Categoria deletada com sucesso",
                null
        );


        return ResponseEntity.ok(response);
    }
}
