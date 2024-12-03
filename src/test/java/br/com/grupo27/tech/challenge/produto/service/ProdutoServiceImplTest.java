package br.com.grupo27.tech.challenge.produto.service;

import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.factory.EntityFactory;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import br.com.grupo27.tech.challenge.produto.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto2;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoRequestDto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoResponseDto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProdutoResponseDto2;
import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.ARQUIVO_VAZIO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl service;

    @Mock
    private  EntityFactory<Produto, ProdutoRequestDto> factory;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

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

    @Test
    void uploadArquivoCsvWithValidFile() throws IOException {
        var file = new MockMultipartFile("file", "produto.csv", "text/csv", "content".getBytes());

        service.uploadArquivoCsv(file);

        var path = Paths.get("src\\main\\resources\\produto.csv");
        assertTrue(Files.exists(path));
        Files.delete(path);
    }

    @Test
    void uploadArquivoCsvWithEmptyFile() {
        var file = new MockMultipartFile("file", "produto.csv", "text/csv", new byte[0]);

        var exception = assertThrows(
                ControllerPropertyReferenceException.class,
                () -> service.uploadArquivoCsv(file)
        );

        assertEquals(ARQUIVO_VAZIO, exception.getMessage());
    }

    @Test
    void startJobSuccessfully() throws Exception {
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(null);

        service.startJob();

        verify(jobLauncher, times(1)).run(any(Job.class), any(JobParameters.class));
    }
}
