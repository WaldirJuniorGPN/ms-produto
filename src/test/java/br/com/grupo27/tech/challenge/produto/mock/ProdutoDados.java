package br.com.grupo27.tech.challenge.produto.mock;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;

import java.time.LocalDateTime;

public interface ProdutoDados {


     String nome = "Produto Dados";
     String descricao = "Produto Dados descricao";
     String sku = "12121-2";
     Categoria categoriaId = Categoria.valueOf("FEMININO");
     double preco = 100.00;
     int quantidadeEstoque = 10;
     double peso = 350;
     Fabricante fabricanteId = Fabricante.valueOf("NIKE");
     boolean status = Boolean.TRUE;
     String imagemPrincipalUrl = "";
     String imagensAdicionaisUrl = "";
     String tags = "";
     String urlAmigavel = ""; //slug
     String metaTitle = "";
     String metaDescrition = "";

     static Produto getProduto(){
         var produto = new Produto();
         produto.setId(1L);
         produto.setNome(nome);
         produto.setDescricao(descricao);
         produto.setSku(sku);
         produto.setPreco(preco);
         produto.setQuantidadeEstoque(quantidadeEstoque);
         produto.setCategoriaId(categoriaId);
         produto.setFabricanteId(fabricanteId);
         produto.setStatus(status);
         produto.setImagemPrincipalUrl(imagemPrincipalUrl);
         produto.setTags(tags);
         produto.setUrlAmigavel(urlAmigavel);
         produto.setImagensAdicionaisUrl(imagensAdicionaisUrl);
         produto.setMetaTitle(metaTitle);
         produto.setMetaDescrition(metaDescrition);
         return produto;
     }

    static Produto getProduto2(){
        var produto = new Produto();
        produto.setId(1L);
        produto.setNome("produto 2");
        produto.setDescricao(descricao);
        produto.setSku("3232323-11");
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produto.setCategoriaId(categoriaId);
        produto.setFabricanteId(fabricanteId);
        produto.setStatus(status);
        produto.setImagemPrincipalUrl(imagemPrincipalUrl);
        produto.setImagensAdicionaisUrl(imagensAdicionaisUrl);
        produto.setTags(tags);
        produto.setUrlAmigavel(urlAmigavel);
        produto.setMetaTitle(metaTitle);
        produto.setMetaDescrition(metaDescrition);
        return produto;
    }

    static ProdutoRequestDto getProdutoRequestDto(){
        var produto = getProduto();
            var produtoRequestDto = new ProdutoRequestDto(produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getSku(),
                    produto.getCategoriaId(),
                    produto.getPreco(),
                    produto.getQuantidadeEstoque(),
                    produto.getPeso(),
                    produto.getFabricanteId(),
                    produto.isStatus(),
                    produto.getImagemPrincipalUrl(),
                    produto.getImagensAdicionaisUrl(),
                    produto.getTags(),
                    produto.getUrlAmigavel(),
                    produto.getMetaTitle(),
                    produto.getMetaDescrition());

            return produtoRequestDto;
    }

    static ProdutoRequestDto getProdutoAlteracaoRequestDto(){
        var produto = getProduto();
        produto.setId(1L);
        produto.setNome("Produto Dados de Alteracao");
        produto.setPreco(699.99);
        var produtoRequestDto = new ProdutoRequestDto(produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getSku(),
                produto.getCategoriaId(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getPeso(),
                produto.getFabricanteId(),
                produto.isStatus(),
                produto.getImagemPrincipalUrl(),
                produto.getImagensAdicionaisUrl(),
                produto.getTags(),
                produto.getUrlAmigavel(),
                produto.getMetaTitle(),
                produto.getMetaDescrition());

        return produtoRequestDto;
    }

    static ProdutoResponseDto getProdutoResponseDto(){
        var produto = getProduto();
        var produtoRequestDto = new ProdutoResponseDto(produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getCategoriaId(),
                produto.getSku(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getPeso(),
                produto.getFabricanteId(),
                produto.isStatus(),
                produto.getImagemPrincipalUrl(),
                produto.getImagensAdicionaisUrl(),
                produto.getTags(),
                produto.getUrlAmigavel(),
                produto.getMetaTitle(),
                produto.getMetaDescrition());

        return produtoRequestDto;
    }

    static ProdutoResponseDto getProdutoResponseDto2(){
        var produto = getProduto2();
        var produtoRequestDto = new ProdutoResponseDto(produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getCategoriaId(),
                produto.getSku(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getPeso(),
                produto.getFabricanteId(),
                produto.isStatus(),
                produto.getImagemPrincipalUrl(),
                produto.getImagensAdicionaisUrl(),
                produto.getTags(),
                produto.getUrlAmigavel(),
                produto.getMetaTitle(),
                produto.getMetaDescrition());

        return produtoRequestDto;
    }
}
