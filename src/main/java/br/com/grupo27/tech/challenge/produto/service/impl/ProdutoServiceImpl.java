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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {


    private final ProdutoRepository repository;
    private final EntityFactory<Produto, ProdutoRequestDto> factory;

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
         return pageProduto.map(produto -> new ProdutoResponseDto(produto));
    }

    @Override
    public ProdutoResponseDto buscarPorId(Long id) {
        var produto = repository.findById(id)
                .map(ProdutoResponseDto::new)
                .orElseThrow(() -> throwPropertyReferenceException());
        return produto;
    }


    @Override
    public ProdutoResponseDto atualizar(Long id,  ProdutoRequestDto dto) {
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
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade) ;
        return new ProdutoResponseDto(produto);
    }

    private ControllerPropertyReferenceException throwPropertyReferenceException() {
        return new ControllerPropertyReferenceException(ConstantesUtils.PRODUTO_NAO_ENCOTRADO);
    }
}
