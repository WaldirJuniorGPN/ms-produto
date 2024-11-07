package br.com.grupo27.tech.challenge.produto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "tb_produto")
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private String sku;
    private Categoria categoriaId;
    private double preco;
    private int quantidadeEstoque;
    private double peso;
    private Fabricante fabricanteId;
    private boolean status;
    private String imagemPrincipalUrl;
    private String imagensAdicionaisUrl;
    private String tags;
    private String urlAmigavel; //slug
    private String metaTitle;
    private String metaDescrition;

}
