package br.com.grupo27.tech.challenge.produto.Controller;

import br.com.grupo27.tech.challenge.produto.controller.ProdutoController;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProdutoControllerTest {

    private MockMvc mockMvc;
    private ProdutoRequestDto produtoRequestDto;
    private ProdutoResponseDto produtoResponseDto;
    private ProdutoResponseDto produtoResponseDto2;
    private Long id;

    @Mock
    private ProdutoService service;

    @InjectMocks
    private ProdutoController controller;


    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        produtoRequestDto = getProdutoRequestDto();
        produtoResponseDto = getProdutoResponseDto();
        produtoResponseDto2 = getProdutoResponseDto2();
        id = 1L;

    }
    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();

    }


    @Test
    void deverPermitirCriarProduto() throws Exception {

        var uriEsperada = "http://localhost/produtos/1";

        when(service.cadastrar(produtoRequestDto)).thenReturn(produtoResponseDto);

        var result = controller.cadastrar(produtoRequestDto, UriComponentsBuilder.fromUriString("http://localhost"));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(uriEsperada, Objects.requireNonNull(result.getHeaders().getLocation()).toString());
        assertEquals(produtoResponseDto, result.getBody());
        verify(service, times(1)).cadastrar(produtoRequestDto);

    }

    @Test
    void deverPermitirBuscarTodosProduto() throws Exception {
        var pageRequest = PageRequest.of(0,10);
        List<ProdutoResponseDto> produtos = List.of(produtoResponseDto, produtoResponseDto2);
        Page<ProdutoResponseDto> produtoPage = new PageImpl<>(produtos, pageRequest, produtos.size());

        when(service.buscarTodos(pageRequest)).thenReturn(produtoPage);

        var result = controller.buscarTodos(pageRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(produtoPage, result.getBody());
        verify(service, times(1)).buscarTodos(pageRequest);
    }

    @Test
    void deverPermitirBuscarProdutoPorId(){
        when(service.buscarPorId(id)).thenReturn(produtoResponseDto);

        var result = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(produtoResponseDto, result.getBody());
        verify(service, times(1)).buscarPorId(id);

    }


    @Test
    void deverPermitirAtualizarProduto(){
        when(service.atualizar(id, produtoRequestDto)).thenReturn(produtoResponseDto);

        var result = controller.atualizar(id, produtoRequestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(produtoResponseDto, result.getBody());
        verify(service, times(1)).atualizar(id, produtoRequestDto);

    }


    @Test
    void deverPermitirRemoverProduto(){
        doNothing().when(service).remover(id);

        controller.deletar(id);

        verify(service, times(1)).remover(id);
    }



}


