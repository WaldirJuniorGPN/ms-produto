package br.com.grupo27.tech.challenge.produto.service;


import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import br.com.grupo27.tech.challenge.produto.service.impl.ProdutoServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.PRODUTO_NAO_ENCOTRADO;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
public class ProdutoServiceImplt {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoServiceImpl produtoService;

    private ProdutoRequestDto produtoRequestDto;

    @BeforeEach
    void setUp() {
        produtoRequestDto = getProdutoRequestDto();
    }

    @Test
    void testCadastrarProduto() {
        var response = produtoService.cadastrar(produtoRequestDto);

        assertNotNull(response);
        assertEquals(produtoRequestDto.nome(), response.nome());
    }

    void testBuscarTodos() {
        var produto = getProduto();
        produto.setId(null);
        repository.save(produto);

        var pageRequest = PageRequest.of(0, 10);

        var page = produtoService.buscarTodos(pageRequest);

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
    }

    void testBuscarPorId_ProdutoExistente() {
        var responseDto = produtoService.cadastrar(produtoRequestDto);

        var foundProduto = produtoService.buscarPorId(responseDto.id());

        assertNotNull(foundProduto);
        assertEquals(responseDto.id(), foundProduto.id());
    }

    @Test
    void testBuscarPorId_ProdutoNaoExistente() {
        var exception = assertThrows(
                ControllerPropertyReferenceException.class,
                () -> produtoService.buscarPorId(999L)
        );
        assertEquals(PRODUTO_NAO_ENCOTRADO, exception.getMessage());
    }

    void testAtualizarProduto() {
        var responseDto = produtoService.cadastrar(produtoRequestDto);

        var updateDto = getProdutoAlteracaoRequestDto();

        var updatedProduto = produtoService.atualizar(responseDto.id(), updateDto);

        assertNotNull(updatedProduto);
        assertEquals("Produto Atualizado", updatedProduto.nome());
        assertEquals(699.99, updatedProduto.preco());
    }

    void testRemoverProduto() {
        var responseDto = produtoService.cadastrar(produtoRequestDto);
        var id = responseDto.id();

        produtoService.remover(id);

        var exception = assertThrows(
                ControllerPropertyReferenceException.class,
                () -> produtoService.buscarPorId(id)
        );
        assertEquals(PRODUTO_NAO_ENCOTRADO, exception.getMessage());
    }

    void testAtualizarEstoque() {
        var responseDto = produtoService.cadastrar(produtoRequestDto);
        var id = responseDto.id();
        int quantidade = 3;

        var updatedProduto = produtoService.atualizarEstoque(id, quantidade);

        assertNotNull(updatedProduto);
        assertEquals(produtoRequestDto.quantidadeEstoque() - quantidade, updatedProduto.quantidadeEstoque());
    }

    @Test
    void testAtualizarEstoque_ProdutoNaoExistente() {
        var exception = assertThrows(
                ControllerPropertyReferenceException.class,
                () -> produtoService.atualizarEstoque(999L, 5)
        );
        assertEquals(PRODUTO_NAO_ENCOTRADO, exception.getMessage());
    }
}
