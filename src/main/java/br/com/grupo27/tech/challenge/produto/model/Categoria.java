package br.com.grupo27.tech.challenge.produto.model;

import lombok.Getter;

@Getter
public enum Categoria {
    MASCULINO(1),
    FEMININO(2),
    INFANTIL(3),
    CALÇADOS(4);

    private final int categoriaId;

    Categoria(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public static Categoria fromId(int id) {
        for (Categoria categoria : values()) {
            if (categoria.getCategoriaId() == id) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + id);
    }
}
