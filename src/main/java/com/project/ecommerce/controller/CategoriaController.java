package com.project.ecommerce.controller;


import com.project.ecommerce.dto.CategoriaResponseDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.exception.CategoriaException;
import com.project.ecommerce.exception.GlobalExceptionHandler;
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
                }).orElseThrow(() -> new CategoriaException("Categoria com ID " + id + " não encontrada!"));

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/nomes/{nome}")
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> listarPorNome(@PathVariable String nome){
        return categoriaService.listarPorNome(nome).map(
                categoria -> {
                    CategoriaResponseDTO categoriaDTO = CategoriaMapper.toDTO(categoria);
                    ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                            true,
                            "Categoria encontrada",
                            categoriaDTO
                    );
                    return ResponseEntity.ok(response);
                }).orElseThrow(() -> new CategoriaException("Categoria com nome " + nome + " não encontrada!"));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> criar(@RequestBody CategoriaResponseDTO dto){
        Categoria salvo = categoriaService.salvarCategoria(CategoriaMapper.toEntity(dto));
        CategoriaResponseDTO categoriaSave = CategoriaMapper.toDTO(salvo);
        Optional<Categoria> nomeExistente = categoriaService.listarPorNome(dto.getNome());

        if (nomeExistente.isPresent()){
            ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                    false,
                    "Categoria com nome " + dto.getNome() + " já existe",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }


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

        Categoria existente = categoriaService.listarPorId(id)
                .orElseThrow(() -> new CategoriaException("Categoria com ID " + id + " não encontrada."));

        Optional<Categoria> nomeExistente = categoriaService.listarPorNome(dto.getNome());
        if (nomeExistente.isPresent()){
            ApiResponse<CategoriaResponseDTO> response = new ApiResponse<>(
                    false,
                    "Categoria com nome " + dto.getNome() + " já existe",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

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
                    "Categoria com " + id + " não encontrada!",
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
