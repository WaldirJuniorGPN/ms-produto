package br.com.grupo27.tech.challenge.produto.service;

import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProdutoService {

    ProdutoResponseDto cadastrar(ProdutoRequestDto dto);

    Page<ProdutoResponseDto> buscarTodos(Pageable pageable);

    ProdutoResponseDto buscarPorId(Long id);

    ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto);

    void remover(Long id);

    ProdutoResponseDto atualizarEstoque(Long id, int quantidade);

    void uploadArquivoCsv(MultipartFile file) throws IOException;

    void startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;
}
