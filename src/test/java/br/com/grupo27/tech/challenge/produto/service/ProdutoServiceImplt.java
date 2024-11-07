package br.com.grupo27.tech.challenge.produto.service;


import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import br.com.grupo27.tech.challenge.produto.service.impl.ProdutoServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;


import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.*;
import static org.assertj.core.api.Assertions.*;



@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ProdutoServiceImplt {

    private Produto produtoSalvo;
    private Long idProdutoSalvo;

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoServiceImpl service;

    AutoCloseable openMock;

    @BeforeEach
    void setUp() {
        openMock = MockitoAnnotations.openMocks(this);

        produtoSalvo = repository.save(getProduto());
        repository.save(getProduto2());
        idProdutoSalvo= produtoSalvo.getId();
    }

    @AfterEach
    void tearDown() throws Exception {

        openMock.close();
    }


    @Test
    void deverPermitirCriarProduto(){
        var produtoRequest = getProdutoRequestDto();

        var result = service.cadastrar(produtoRequest);

        assertThat(result).isNotNull().isInstanceOf(ProdutoResponseDto.class);
        assertThat(result.id()).isNotNull();

    }

    @Test
    void deverPermitirBuscarTodosProduto(){
       var pageRequest = PageRequest.of(0,10);

       var result = service.buscarTodos(pageRequest);

       assertThat(result)
               .isNotNull()
               .hasSize(2);
    }


    @Test
    public void deveBuscarProdutoPorId() {
        var id = idProdutoSalvo;

        var result = service.buscarPorId(id);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.nome()).isEqualTo(produtoSalvo.getNome());
    }

    @Test
    void deveGerarExcecao_QuandoBuscarProdutoPorId_IdNaoExiste(){
        var id =1L;

        assertThatThrownBy(
                () -> service.buscarPorId(id))
                .isInstanceOf(ControllerPropertyReferenceException.class)
                .hasMessage("Produto não encontrado");
    }


    @Test
    void deverPermitirAtualizarProduto(){


        var id = idProdutoSalvo;
        var produtoRequestDto = getProdutoAlteracaoRequestDto();

        var result = service.atualizar(id, produtoRequestDto);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(ProdutoResponseDto.class);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.nome()).isEqualTo(" Produto Dados de Alteracao");

    }

    @Test
    void deveGerarExcecao_QuandoAtualizarProduto_IdNaoExiste(){
        var id = 1L;

        assertThatThrownBy(
                () -> service.buscarPorId(id))
                .isInstanceOf(ControllerPropertyReferenceException.class)
                .hasMessage("Produto não encontrado");
    }

    @Test
    void deverPermitirRemoverProduto(){

        var id = idProdutoSalvo;

        service.remover(id);

        assertThat(getProduto().getId()).isNotNull();
    }

    @Test
    void deveGerarExcecao_QuandoRemover_IdNaoExiste(){
        var id = 1L;

        assertThatThrownBy(
                () -> service.buscarPorId(id))
                .isInstanceOf(ControllerPropertyReferenceException.class)
                .hasMessage("Produto não encontrado");
    }
}
