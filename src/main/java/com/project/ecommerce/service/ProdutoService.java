package com.project.ecommerce.service;

import com.project.ecommerce.entity.ProdutoEntity;
import com.project.ecommerce.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository= produtoRepository;
    }

    public Page<ProdutoEntity> listarTodosProdutos(Pageable pageable){
        return  produtoRepository.findAll(pageable);
    }

    public List<ProdutoEntity> salvarTodosProdutos(List<ProdutoEntity> produtos){
        return produtoRepository.saveAll(produtos);
    }

    public Optional<ProdutoEntity> buscarPorIdProduto(Long id){
        return produtoRepository.findById(id);
    }

    public Optional<ProdutoEntity> buscarPorNomeProduto(String nome) {
        return produtoRepository.findByNome(nome);
    }

    public ProdutoEntity salvarProduto(ProdutoEntity produto){
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id){
        produtoRepository.deleteById(id);
    }




}
