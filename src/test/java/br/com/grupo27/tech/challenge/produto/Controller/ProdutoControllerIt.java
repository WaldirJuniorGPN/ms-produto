package br.com.grupo27.tech.challenge.produto.Controller;

import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProdutoControllerIt {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;


    @Test
    void deverPermitirCriarProduto() throws Exception {

        when(service.cadastrar(any(ProdutoRequestDto.class))).thenReturn(getProdutoResponseDto());


        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(getProdutoRequestDto())))
                .andExpect(status().isCreated());
    }

    @Test
    void deverPermitirBuscarTodosProduto() throws Exception {
        List<ProdutoResponseDto> produtos = List.of(getProdutoResponseDto(), getProdutoResponseDto2());
        var produtosPage = new PageImpl<>(produtos);

        when(service.buscarTodos(any())).thenReturn(produtosPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/produtos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void deverPermitirBuscarProdutoPorId() throws Exception {
        var id = 1L;
        when(service.buscarPorId(id)).thenReturn(getProdutoResponseDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/produtos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    void deverPermitirAtualizarProduto() throws Exception {
        var id = 1L;
        var produtoRequestDto = getProdutoRequestDto();
        when(service.atualizar(id, produtoRequestDto )).thenReturn(getProdutoResponseDto());

        mockMvc.perform(MockMvcRequestBuilders.put("/produtos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(produtoRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deverPermitirRemoverProduto() throws Exception {
        var id = 1L;
        Mockito.doNothing().when(service).remover(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/produtos/{id}",id))
                .andExpect(status().isNoContent());
    }



    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
