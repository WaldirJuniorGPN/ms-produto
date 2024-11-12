package br.com.grupo27.tech.challenge.produto.factory.impl;

import br.com.grupo27.tech.challenge.produto.factory.EntityFactory;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static br.com.grupo27.tech.challenge.produto.utils.UpdateUtils.atualizarCampo;

@Component
@Qualifier("ProdutoFactory")
@RequiredArgsConstructor
public class ProdutoFactoryImpl implements EntityFactory<Produto, ProdutoRequestDto> {
    @Override
    public Produto criar(ProdutoRequestDto dto) {
        var produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setCategoriaId(dto.categoriaId());
        produto.setSku(dto.sku());
        produto.setPreco(dto.preco());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setPeso(dto.peso());
        produto.setFabricanteId(dto.fabricanteId());
        produto.setStatus(dto.status());
        produto.setImagemPrincipalUrl(dto.imagemPrincipalUrl());
        produto.setImagensAdicionaisUrl(dto.imagensAdicionaisUrl());
        produto.setTags(dto.tags());
        produto.setUrlAmigavel(dto.urlAmigavel());
        produto.setMetaTitle(dto.metaTitle());
        produto.setMetaDescrition(dto.metaDescrition());
        return produto;
    }

    @Override
    public void atualizar(Produto produto, ProdutoRequestDto dto) {
        List<Runnable> updates = Arrays.asList(
                () -> atualizarCampo(produto::getNome,   produto::setNome, dto.nome()),
                () -> atualizarCampo(produto::getDescricao, produto::setDescricao, dto.descricao()),
                () -> atualizarCampo(produto::getCategoriaId, produto::setCategoriaId, dto.categoriaId()),
                () -> atualizarCampo(produto::getSku, produto::setSku, dto.sku()),
                () -> atualizarCampo(produto::getPreco, produto::setPreco, dto.preco()),
                () -> atualizarCampo(produto::getFabricanteId, produto::setFabricanteId, dto.fabricanteId()),
                () -> atualizarCampo(produto::getPeso, produto::setPeso, dto.peso()),
                () -> atualizarCampo(produto::getQuantidadeEstoque, produto::setQuantidadeEstoque, dto.quantidadeEstoque()),
                () -> atualizarCampo(produto::isStatus, produto::setStatus, dto.status()),
                () -> atualizarCampo(produto::getImagemPrincipalUrl, produto::setImagemPrincipalUrl, dto.imagemPrincipalUrl()),
                () -> atualizarCampo(produto::getTags, produto::setTags, dto.tags()),
                () -> atualizarCampo(produto::getMetaTitle, produto::setMetaTitle, dto.metaTitle()),
                () -> atualizarCampo(produto::getMetaDescrition, produto::setMetaDescrition, dto.metaDescrition()),
                () -> atualizarCampo(produto::getUrlAmigavel, produto::setUrlAmigavel, dto.urlAmigavel())
        );
        updates.forEach(Runnable::run);
    }
}
