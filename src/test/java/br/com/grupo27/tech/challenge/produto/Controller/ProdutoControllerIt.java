package br.com.grupo27.tech.challenge.produto.Controller;

import br.com.grupo27.tech.challenge.produto.controller.ProdutoController;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@ActiveProfiles("test")
public class ProdutoControllerIt {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job job;

    @Autowired
    private ObjectMapper objectMapper;

    private ProdutoResponseDto responseDto;
    private ProdutoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = getProdutoRequestDto();
        responseDto = getProdutoResponseDto();
    }

    @Test
    void cadastrarProduto() throws Exception {

        when(produtoService.cadastrar(any(ProdutoRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/produtos/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Dados"))
                .andExpect(jsonPath("$.preco").value(100.00))
                .andExpect(jsonPath("$.quantidadeEstoque").value(10));
    }

    @Test
    void buscarTodosProdutos() throws Exception {
        Page<ProdutoResponseDto> produtos = new PageImpl<>(
                Collections.singletonList(responseDto),
                PageRequest.of(0, 10),
                1
        );

        when(produtoService.buscarTodos(any(PageRequest.class))).thenReturn(produtos);

        mockMvc.perform(get("/produtos").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Produto Dados"));
    }

    @Test
    void buscarProdutoPorId() throws Exception {
        when(produtoService.buscarPorId(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Dados"));
    }

    @Test
    void atualizarProduto() throws Exception {

        var produtoAtualizado = getProdutoAlteracaoRequestDto();
        var responseAtualizado = getProdutoAlteracaoResponseDto();

        when(produtoService.atualizar(any(Long.class), any(ProdutoRequestDto.class))).thenReturn(responseAtualizado);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Dados de Alteracao"))
                .andExpect(jsonPath("$.preco").value(699.99));
    }

    @Test
    void deletarProduto() throws Exception {
        doNothing().when(produtoService).remover(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizarEstoqueProduto() throws Exception {

        when(produtoService.atualizarEstoque(1L, 50)).thenReturn(responseDto);

        mockMvc.perform(patch("/produtos/atualizar/estoque/1/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantidadeEstoque").value(10));
    }

    @Test
    void importaArquivo() throws Exception {
        when(jobLauncher.run(any(Job.class), any())).thenReturn(mock(JobExecution.class));

        mockMvc.perform(get("/produtos/importacao"))
                .andExpect(status().isOk());
    }

}
