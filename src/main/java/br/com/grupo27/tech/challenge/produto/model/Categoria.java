package br.com.grupo27.tech.challenge.produto.model;

public enum Categoria {
    MASCULINO(1),
    FEMININO(2),
    INFANTIL(3),
    CALÇADOS(4);

    private final int categoriaId;

    Categoria(int categoriaId){
        this.categoriaId = categoriaId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }
}
