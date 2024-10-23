package br.com.grupo27.tech.challenge.produto.factory.impl;

import br.com.grupo27.tech.challenge.produto.factory.EntityFactory;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
      //  produto.setDataCriacao(dto.dataCriacao());
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

        if(!produto.getNome().equals(dto.nome())) {
            produto.setNome(dto.nome());
        }
        if(!produto.getDescricao().equals(dto.descricao())) {
            produto.setDescricao(dto.descricao());
        }
        if(!produto.getCategoriaId().equals(dto.categoriaId())) {
            produto.setCategoriaId(dto.categoriaId());
        }
        if(!produto.getSku().equals(dto.sku())) {
            produto.setSku(dto.sku());
        }
        if (produto.getPreco() != dto.preco()) {
            produto.setPreco(dto.preco());
        }
        if (produto.getFabricanteId() != dto.fabricanteId()) {
            produto.setFabricanteId(dto.fabricanteId());
        }
        if(produto.getQuantidadeEstoque() != dto.quantidadeEstoque()) {
            produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        }
        if(produto.getPeso() != dto.peso()) {
            produto.setPeso(dto.peso());
        }
        if(produto.isStatus() != dto.status()) {
            produto.setStatus(dto.status());
        }
//        if(produto.getDataCriacao() != dto.dataCriacao()) {
//            produto.setDataCriacao(dto.dataCriacao());
//        }
        if(produto.getImagemPrincipalUrl() != dto.imagemPrincipalUrl()) {
            produto.setImagemPrincipalUrl(dto.imagemPrincipalUrl());
        }
        if(produto.getTags() != dto.tags()) {
            produto.setTags(dto.tags());
        }
        if(produto.getMetaTitle() != dto.metaTitle()) {
            produto.setMetaTitle(dto.metaTitle());
        }
        if(produto.getMetaDescrition() != dto.metaDescrition()) {
            produto.setMetaDescrition(dto.metaDescrition());
        }
        if(produto.getUrlAmigavel() != dto.urlAmigavel()) {
            produto.setUrlAmigavel(dto.urlAmigavel());
        }
        if(produto.getMetaTitle() != dto.metaTitle()) {
            produto.setMetaTitle(dto.metaTitle());
        }
        if(produto.getMetaDescrition() != dto.metaDescrition()) {
            produto.setMetaDescrition(dto.metaDescrition());
        }

    }
}
