package br.com.grupo27.tech.challenge.produto.service;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    ProdutoResponseDto cadastrar(ProdutoRequestDto dto);

    Page<ProdutoResponseDto> buscarTodos(Pageable pageable);

    ProdutoResponseDto buscarPorId(Long id);

    ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto);

    void remover(Long id);

    ProdutoResponseDto atualizarEstoque(Long id, int quantidade);
}
