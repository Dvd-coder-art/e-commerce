package com.project.ecommerce.controller;


import com.project.ecommerce.dto.ProdutoResponseDTO;
import com.project.ecommerce.entity.Categoria;
import com.project.ecommerce.entity.ProdutoEntity;
import com.project.ecommerce.exception.CategoriaException;
import com.project.ecommerce.exception.ProdutoNaoEncontradoException;
import com.project.ecommerce.mapper.ProdutoMapper;
import com.project.ecommerce.response.ApiResponse;
import com.project.ecommerce.service.CategoriaService;
import com.project.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<Page<ProdutoResponseDTO>>> listarTodos(Pageable pageable){
        Page<ProdutoEntity> pagina = produtoService.listarTodosProdutos(pageable);
        Page<ProdutoResponseDTO> paginaDTO = pagina.map(ProdutoMapper::toDTO);

        ApiResponse<Page<ProdutoResponseDTO>> resposta = new ApiResponse<>(
                true,
                "Produtos listados com sucesso",
                paginaDTO
        );
        return ResponseEntity.ok(resposta);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> buscarPorId(@PathVariable Long id){
        return produtoService.buscarPorIdProduto(id)
                .map(produto -> {
                    ProdutoResponseDTO produtoRequestDTO = ProdutoMapper.toDTO(produto);
                    ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
                            true,
                            "Produto localizado com sucesso",
                            produtoRequestDTO
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> criar(@RequestBody ProdutoResponseDTO dto){
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (categoria.isEmpty()){
            ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }


        ProdutoEntity entity = ProdutoMapper.toEntity(dto, categoria.get());
        ProdutoEntity salvo = produtoService.salvarProduto(entity);
        ProdutoResponseDTO salvoDTO = ProdutoMapper.toDTO(salvo);
        ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
                true,
                "Produto criado com sucesso",
                salvoDTO

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lote")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> criarEmLote(@RequestBody List<@Valid ProdutoResponseDTO> produtos){
        List<ProdutoEntity> entidades = produtos.stream()
                .map(produto -> {
                    Optional<Categoria> categoria = categoriaService.listarPorId(produto.getCategoriaId());
                    if (categoria.isEmpty()){
                        throw new CategoriaException("Categoria com ID " + produto.getCategoriaId() + " não encontrada!");
                    }
                    return ProdutoMapper.toEntity(produto, categoria.get());
                })
                .collect(Collectors.toList());

        List<ProdutoEntity> salvos = produtoService.salvarTodosProdutos(entidades);
        List<ProdutoResponseDTO> salvoDTOs = salvos.stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());

        ApiResponse<List<ProdutoResponseDTO>> response = new ApiResponse<>(
                true,
                "Produtos cadastrados com sucesso",
                salvoDTOs
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(@PathVariable Long id, @RequestBody ProdutoResponseDTO dto){
        Optional<ProdutoEntity> existente = produtoService.buscarPorIdProduto(id);
        Optional<Categoria> categoria = categoriaService.listarPorId(dto.getCategoriaId());
        if (existente.isEmpty()){
            ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
                    false,
                    "Produto não encontrado",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (categoria.isEmpty()) {
            ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
                    false,
                    "Categoria não encontrada",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        ProdutoEntity atualizado = ProdutoMapper.toEntity(dto,categoria.get());
        atualizado.setId(id);
        ProdutoEntity salvo = produtoService.salvarProduto(atualizado);
        ProdutoResponseDTO salvoDTO = ProdutoMapper.toDTO(salvo);

        ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>(
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
