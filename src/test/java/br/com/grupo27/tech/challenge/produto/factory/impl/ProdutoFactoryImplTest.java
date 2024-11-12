package br.com.grupo27.tech.challenge.produto.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoFactoryImplTest {

    private ProdutoFactoryImpl produtoFactory;

    @BeforeEach
    void setUp() {
        produtoFactory = new ProdutoFactoryImpl();
    }

    @Test
    void testCriarProduto() {
        var dto = getProdutoRequestDto();

        var produto = produtoFactory.criar(dto);

        assertEquals(dto.nome(), produto.getNome());
        assertEquals(dto.descricao(), produto.getDescricao());
        assertEquals(dto.categoriaId(), produto.getCategoriaId());
        assertEquals(dto.sku(), produto.getSku());
        assertEquals(dto.preco(), produto.getPreco());
        assertEquals(dto.quantidadeEstoque(), produto.getQuantidadeEstoque());
        assertEquals(dto.peso(), produto.getPeso());
        assertEquals(dto.fabricanteId(), produto.getFabricanteId());
        assertEquals(dto.status(), produto.isStatus());
        assertEquals(dto.imagemPrincipalUrl(), produto.getImagemPrincipalUrl());
        assertEquals(dto.imagensAdicionaisUrl(), produto.getImagensAdicionaisUrl());
        assertEquals(dto.tags(), produto.getTags());
        assertEquals(dto.urlAmigavel(), produto.getUrlAmigavel());
        assertEquals(dto.metaTitle(), produto.getMetaTitle());
        assertEquals(dto.metaDescrition(), produto.getMetaDescrition());
    }

    @Test
    void testAtualizarProduto() {
        var produto = getProduto();
        var dto = getProdutoAlteracaoRequestDto();

        produtoFactory.atualizar(produto, dto);

        assertEquals("Produto Dados de Alteracao", produto.getNome());
        assertEquals("Produto Dados descricao", produto.getDescricao());
        assertEquals(dto.preco(), produto.getPreco());
    }
}