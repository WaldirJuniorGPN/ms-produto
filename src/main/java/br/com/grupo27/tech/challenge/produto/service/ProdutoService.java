package br.com.grupo27.tech.challenge.produto.service;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    ProdutoResponseDto cadastrar(ProdutoRequestDto dto);

    Page<ProdutoResponseDto> buscarTodos(Pageable pageable);

    ProdutoResponseDto buscarPorId(Integer id);

    ProdutoResponseDto atualizar(Integer id, ProdutoRequestDto dto);

    void remover(Integer id);

    ProdutoResponseDto atualizarEstoque(Integer id, int quantidade);
}