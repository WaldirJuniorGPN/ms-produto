package br.com.grupo27.tech.challenge.produto.model.dto.response;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import br.com.grupo27.tech.challenge.produto.model.Produto;

import java.time.LocalDateTime;


public record ProdutoResponseDto(Long id, String nome, String descricao, Categoria categoriaId,
                                 String sku, double preco, int quantidadeEstoque,
                                 double peso, Fabricante fabricanteId, boolean status,
                                 String imagemPrincipalUrl,
                                 String imagensAdicionaisUrl, String tags,
                                 String urlAmigavel, String metaTitle,
                                 String metaDescrition) {

    public ProdutoResponseDto(Produto produto) {
      this( produto.getId(),
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
    }
}
