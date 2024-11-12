package br.com.grupo27.tech.challenge.produto.service;

import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.factory.EntityFactory;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import br.com.grupo27.tech.challenge.produto.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl service;

    @Mock
    private  EntityFactory<Produto, ProdutoRequestDto> factory;

    @Mock
    private ProdutoRepository repository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

    }
    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deverPermitirCriarProduto(){
        //Arrange
        var produtoRequestDto = getProdutoRequestDto();
        var produto = getProduto();


        when(factory.criar(any(ProdutoRequestDto.class))).thenReturn(produto);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        //Act
        var result = service.cadastrar(produtoRequestDto);

        //Assert
        assertThat(result).isNotNull().isEqualTo(getProdutoResponseDto());
        verify(repository, times(1)).save(any(Produto.class));

    }

    @Test
    void deverPermitirBuscarTodosProduto(){
        var pageRequest = PageRequest.of(0,10);
        var produtoPage = new PageImpl<>(List.of(getProduto(), getProduto2()), pageRequest , 2 );

        when(repository.findAll(any(Pageable.class))).thenReturn(produtoPage);

        var result = service.buscarTodos(pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(repository, times(1)).findAll(pageRequest);
    }

    @Test
    void deverPermitirBuscarProdutoPorId(){
        var id = 1L;
        var produto = getProduto();

        when(repository.findById(id)).thenReturn(Optional.of(produto));

        var result = service.buscarPorId(id);

        assertThat(result).isEqualTo(getProdutoResponseDto());
        verify(repository, times(1)).findById(id);

    }

    @Test
    void deveGerarExcecao_QuandoBuscarProdutoPorId_IdNaoExiste(){

        var id =1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(ControllerPropertyReferenceException.class,
                () -> service.buscarPorId(id));

        assertEquals("Produto não encontrado", exception.getMessage());

    }


    @Test
    void deverPermitirAtualizarProduto(){
        var id = 1L;
        var produtoRequestDto = getProdutoRequestDto();
        var produtoAntesDaAlteracao = getProduto2();
        factory.atualizar(produtoAntesDaAlteracao, produtoRequestDto);

        var produtoAlterado = produtoAntesDaAlteracao;

        when(repository.findById(id)).thenReturn(Optional.of(produtoAntesDaAlteracao));
        when(repository.save(produtoAntesDaAlteracao)).thenReturn(produtoAlterado);

        var result = service.atualizar(id, produtoRequestDto);

        assertNotNull(result);
        assertEquals(getProdutoResponseDto2(), result);
        verify(repository, times(1)).save(produtoAntesDaAlteracao);


    }

    @Test
    void deveGerarExcecao_QuandoAtualizarProduto_IdNaoExiste(){

        var id =1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(ControllerPropertyReferenceException.class,
                () -> service.buscarPorId(id));

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Produto.class));

    }

    @Test
    void deverPermitirRemoverProduto(){

        var id =1L;

        when(repository.findById(id)).thenReturn(Optional.of(getProduto()));

        service.remover(id);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deveGerarExcecao_QuandoRemover_IdNaoExiste(){

        var id =1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(ControllerPropertyReferenceException.class,
                () -> service.buscarPorId(id));

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).deleteById(id);

    }
}
