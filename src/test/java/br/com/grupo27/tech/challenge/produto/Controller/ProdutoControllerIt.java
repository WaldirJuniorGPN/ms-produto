package br.com.grupo27.tech.challenge.produto.Controller;

import br.com.grupo27.tech.challenge.produto.controller.ProdutoController;
import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoAlteracaoRequestDto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoAlteracaoResponseDto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoRequestDto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoResponseDto;
import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.ARQUIVO_VAZIO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
@ActiveProfiles("test")
class ProdutoControllerIt {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

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
        var file = new MockMultipartFile("file", "temp-produto.csv", "text/csv", "content".getBytes());

        doNothing().when(produtoService).uploadArquivoCsv(any());
        doNothing().when(produtoService).startJob();

        mockMvc.perform(multipart("/produtos/importacao").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo importado com sucesso"));
    }

    @Test
    void importarProdutosComErro() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "temp-produto.csv", "text/csv", "content".getBytes());

        doThrow(new RuntimeException("Erro ao importar arquivo")).when(produtoService).uploadArquivoCsv(any());

        mockMvc.perform(multipart("/produtos/importacao").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao importar arquivo"));
    }

    @Test
    void importarProdutosArquivoNaoEncontrado() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "temp-produto.csv", "text/csv", "content".getBytes());

        doThrow(new ControllerPropertyReferenceException(ARQUIVO_VAZIO)).when(produtoService).uploadArquivoCsv(any());

        mockMvc.perform(multipart("/produtos/importacao").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao importar arquivo"));
    }

}
