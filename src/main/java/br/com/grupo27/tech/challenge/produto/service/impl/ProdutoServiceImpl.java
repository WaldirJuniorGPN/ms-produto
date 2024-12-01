package br.com.grupo27.tech.challenge.produto.service.impl;

import br.com.grupo27.tech.challenge.produto.exception.ControllerPropertyReferenceException;
import br.com.grupo27.tech.challenge.produto.factory.EntityFactory;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.repository.ProdutoRepository;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils;
import jakarta.transaction.Transactional;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.ARQUIVO_VAZIO;
import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.QUANTIDADE_ESTOQUE_NEGATIVA;

@Service
public class ProdutoServiceImpl implements ProdutoService {


    private final ProdutoRepository repository;
    private final EntityFactory<Produto, ProdutoRequestDto> factory;
    private final JobLauncher jobLauncher;
    private final Job job;

    public ProdutoServiceImpl(ProdutoRepository repository, EntityFactory<Produto, ProdutoRequestDto> factory, JobLauncher jobLauncher, @Qualifier("job") Job job) {
        this.repository = repository;
        this.factory = factory;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    @Transactional
    public ProdutoResponseDto cadastrar(ProdutoRequestDto dto) {
        var produto = factory.criar(dto);
        repository.save(produto);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public Page<ProdutoResponseDto> buscarTodos(Pageable pageable) {
        Page<Produto> pageProduto = repository.findAll(pageable);
        return pageProduto.map(ProdutoResponseDto::new);
    }

    @Override
    public ProdutoResponseDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(ProdutoResponseDto::new)
                .orElseThrow(this::throwPropertyReferenceException);
    }


    @Override
    public ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto) {
        var produtoASerAlterado = repository.findById(id).orElseThrow(this::throwPropertyReferenceException);
        factory.atualizar(produtoASerAlterado, dto);
        repository.save(produtoASerAlterado);
        return new ProdutoResponseDto(produtoASerAlterado);
    }

    @Override
    public void remover(Long id) {
        var produto = repository.findById(id).orElseThrow(this::throwPropertyReferenceException);
        repository.deleteById(produto.getId());
    }

    @Override
    public ProdutoResponseDto atualizarEstoque(Long id, int quantidade) {
        var produto = repository.findById(id).orElseThrow(this::throwPropertyReferenceException);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        if (produto.getQuantidadeEstoque() < 0) {
            throw new ControllerPropertyReferenceException(QUANTIDADE_ESTOQUE_NEGATIVA);
        }
        repository.save(produto);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public void uploadArquivoCsv(MultipartFile file) throws IOException {

        if(file.isEmpty()){
            throw new ControllerPropertyReferenceException(ARQUIVO_VAZIO);
        }

        try{
            var pastaDestino= new File("src\\main\\resources");
            var diretorio = Paths.get(pastaDestino.toURI());
            var caminhoDoArquivo = diretorio.resolve("produtos.csv");

            Files.write(caminhoDoArquivo, file.getBytes());
        } catch (IOException e) {
            throw e;
        }
    }

    @Async
    @Override
    public void startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        var jobParameters = new JobParameters();
        jobLauncher.run(job, jobParameters);
    }

    private ControllerPropertyReferenceException throwPropertyReferenceException() {
        return new ControllerPropertyReferenceException(ConstantesUtils.PRODUTO_NAO_ENCOTRADO);
    }
}
