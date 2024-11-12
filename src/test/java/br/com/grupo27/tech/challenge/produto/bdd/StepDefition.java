package br.com.grupo27.tech.challenge.produto.bdd;


import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class StepDefition {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ProdutoRepository repository;

    private MvcResult resultado;
    private String jsonRequest;
    private ProdutoResponseDto produtoResponse;
    private Produto produtoCadastro;
    private Long id;
    private int quantidade;

    private Produto criarProduto() throws Exception {
        var produtoRequest = getProdutoRequestDto();
        jsonRequest = objectMapper.writeValueAsString(produtoRequest);
        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();
        var jsonResponse = resultado.getResponse().getContentAsString();
        var produto = objectMapper.readValue(jsonResponse, Produto.class);
        return produto;
    }



    //Cenario: Cadastrar um novo produto com sucesso
    @Dado("que tenha os dados válidos do produto")
    public void que_tenha_os_dados_válidos_do_produto() throws JsonProcessingException {
        var produtoRequest = getProdutoRequestDto();
        jsonRequest = objectMapper.writeValueAsString(produtoRequest);

    }
    @Quando("eu envio uma requisição POST para {string}")
    public void eu_envio_uma_requisição_post_para(String url) throws Exception {
        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();


    }
    @Entao("o produto deve ser criado com sucesso")
    public void o_produto_deve_ser_criado_com_sucesso() throws Exception{
        var jsonResponse = resultado.getResponse().getContentAsString();
        produtoResponse = objectMapper.readValue(jsonResponse, ProdutoResponseDto.class);
        assertNotNull(produtoResponse);
    }
    @Entao("a resposta deve conter os dados do produto criado")
    public void a_resposta_deve_conter_os_dados_do_produto_criado() {
        assertEquals(getProdutoResponseDto(), produtoResponse);
    }

    //Cenario: Atualizar um produto existente com sucesso
    @Dado("que tenha os dados a serem atualizados de um produto existente")
    public void que_tenha_os_dados_a_serem_atualizados_de_um_produto_existente() throws Exception {
        produtoCadastro = criarProduto();
        id = produtoCadastro.getId();
        var produtoRequest = getProdutoAlteracaoRequestDto();
        jsonRequest = objectMapper.writeValueAsString(produtoRequest);

    }
    @Quando("envio uma requisição PUT para {string}")
    public void envio_uma_requisição_put_para(String url) throws Exception {

        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .put(url, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
               .andExpect(status().isOk())
                .andReturn();
    }
    @Entao("o produto deve ser atualizado com sucesso")
    public void o_produto_deve_ser_atualizado_com_sucesso() throws Exception {
        var jsonResponse = resultado.getResponse().getContentAsString();
        produtoResponse = objectMapper.readValue(jsonResponse, ProdutoResponseDto.class);

        assertNotNull(produtoResponse);
        assertEquals(id, produtoResponse.id());
    }
    @Entao("a resposta deve conter os dados atualizados do produto")
    public void a_resposta_deve_conter_os_dados_atualizados_do_produto(){
        assertEquals("Produto Dados de Alteracao", produtoResponse.nome());
    }

    //Cenario: Buscar um produto existente com sucesso

    @Dado("que tenha um ID de um produto existente")
    public void que_tenha_um_id_de_um_produto_existente() throws Exception {
        produtoCadastro = criarProduto();
        id = produtoCadastro.getId();
        assertNotNull(id);

    }
    @Quando("envio uma requisição GET para {string}")
    public void envio_uma_requisição_get_para(String url) throws Exception {
        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get(url, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Entao("o status de resposta deve ser {int}")
    public void o_status_de_resposta_deve_ser(Integer int1) throws Exception {
        var jsonResponse = resultado.getResponse().getContentAsString();
        produtoResponse = objectMapper.readValue(jsonResponse, ProdutoResponseDto.class);

        assertNotNull(produtoResponse);
        assertEquals(id, produtoResponse.id());
    }

    //Cenario: Listar todos os produtos com sucesso

    @Dado("que existam produtos cadastrados no sistema")
    public void que_existam_produtos_cadastrados_no_sistema() throws Exception {
       produtoCadastro = criarProduto();
       assertNotNull(produtoCadastro);

    }

    @Quando("envio uma requisição GET para sem parametros {string}")
    public void envio_uma_requisição_get_para_sem_parametros(String url) throws Exception {
        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Entao("uma lista de produtos deve ser retornada")
    public void uma_lista_de_produtos_deve_ser_retornada() throws Exception {
        var jsonResponse = resultado.getResponse().getContentAsString();
        var produtoList = objectMapper.readValue(jsonResponse, ProdutoResponseDto.class);

        assertNotNull(produtoList);

    }

    //Cenario: Atualizar a quantidade de um produto em estoque
    @Dado("que tenho o ID de um produto e a quantidade para baixar do estoque")
    public void que_tenho_o_id_de_um_produto_e_a_quantidade_para_baixar_do_estoque() throws Exception {
        produtoCadastro = criarProduto();
        id = produtoCadastro.getId();
        assertNotNull(id);
        quantidade = 1;

    }
    @Quando("envio de uma requisição PUT para {string}")
    public void envio_de_uma_requisição_put_para(String url) throws Exception {


        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .put(url, id, quantidade)
                )
                .andExpect(status().isOk())
                .andReturn();
    }
    @Entao("quantidade do produto com id informado dever ser subtraido pela quantidade informada na requisiçao")
    public void quantidade_do_produto_com_id_informado_dever_ser_subtraido_pela_quantidade_informada_na_requisiçao() throws Exception {
        var jsonResponse = resultado.getResponse().getContentAsString();
        produtoResponse = objectMapper.readValue(jsonResponse, ProdutoResponseDto.class);

        assertNotNull(produtoResponse);
        assertEquals(9, produtoResponse.quantidadeEstoque());
    }

    //Cenario: Deletar um produto existente com sucesso

    @Dado("que tenha o ID de um produto existente")
    public void que_tenha_o_id_de_um_produto_existente() throws Exception {
        produtoCadastro = criarProduto();
        id = produtoCadastro.getId();
        assertNotNull(id);
    }
    @Quando("envio uma requisição DELETE para {string}")
    public void envio_uma_requisição_delete_para(String url) throws Exception {
        resultado = mockMvc.perform(MockMvcRequestBuilders
                        .delete(url, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
    @Entao("o produto dever ser excluido com sucesso")
    public void o_produto_dever_ser_excluido_com_sucesso() {
        assertNotNull(id);
    }













}