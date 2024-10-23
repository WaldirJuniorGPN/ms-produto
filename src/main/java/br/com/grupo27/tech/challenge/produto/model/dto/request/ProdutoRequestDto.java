package br.com.grupo27.tech.challenge.produto.model.dto.request;


import br.com.grupo27.tech.challenge.produto.model.Categoria;
import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ProdutoRequestDto(

        Long id,
        @NotBlank(message = "o campo nome não pode ser nulo")
        String nome,
        String descricao,
        String sku,
        @NotBlank(message = "o campo Categoria dever ser informado")
        Categoria categoriaId,
        double preco,
        int quantidadeEstoque,
        double peso,
        @NotBlank(message = "o campo fabricante dever ser informado")
        Fabricante fabricanteId,
        @NotBlank(message = "o campo status dever ser informado")
        boolean status,

        String imagemPrincipalUrl,
        String imagensAdicionaisUrl,
        String tags,
        String urlAmigavel,
        String metaTitle,
        String metaDescrition

) {
}
