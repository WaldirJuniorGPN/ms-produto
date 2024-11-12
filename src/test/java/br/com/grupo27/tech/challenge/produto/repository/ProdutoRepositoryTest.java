package br.com.grupo27.tech.challenge.produto.repository;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoRepositoryTest {

    @Mock
    private ProdutoRepository produtoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deverPermitirCriarProduto() {
        //Arrange
        var produto = getProduto();

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        //Act
        var result = produtoRepository.save(produto);
        //Assert
        assertThat(result)
            .isNotNull()
            .isEqualTo(produto);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deverPermitirBuscarProduto() {
        //Arrange
        var produto = getProduto();


        when(produtoRepository.findById(getProduto().getId())).thenReturn(Optional.of(produto));

        //Act
        var result = produtoRepository.findById(getProduto().getId());

        //Assert
        assertThat(result)
                .isPresent()
                .containsSame(produto);

        verify(produtoRepository, times(1)).findById(getProduto().getId());
    }

    @Test
    void deverRemoverProduto() {
        //Arrange
        var id = 1L;

        doNothing().when(produtoRepository).deleteById(id);
        //Act
        produtoRepository.deleteById(id);
        //Assert
        verify(produtoRepository, times(1)).deleteById(id);
    }

    @Test
    void deverPermitirListarProdutos(){
        //Arrange
        var listaProdutos = Arrays.asList(getProduto(), getProduto2());

        when(produtoRepository.findAll()).thenReturn(listaProdutos);
        //act
        var result = produtoRepository.findAll();
        //Assert
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(getProduto(), getProduto2());

        verify(produtoRepository, times(1)).findAll();
    }
}
