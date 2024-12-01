package br.com.grupo27.tech.challenge.produto.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantesUtils {
    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String PROPERTY_REFERENCE_INVALID = "Property reference invalid";
    public static final String ERRO_VALIDACAO = "Erro de Validação";
    public static final String PRODUTO_NAO_ENCOTRADO = "Produto não encontrado";
    public static final String ARQUIVO_VAZIO = "Arquivo vazio";
    public static final String QUANTIDADE_ESTOQUE_NEGATIVA = "Quantidade em estoque não pode ser negativa";
}
