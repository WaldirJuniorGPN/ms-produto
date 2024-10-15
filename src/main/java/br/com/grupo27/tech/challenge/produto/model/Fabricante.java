package br.com.grupo27.tech.challenge.produto.model;

public enum Fabricante {

    NIKE(1),
    ADIDAS(2),
    FILA(3),
    VANS(4),
    ;

    private final int fabricanteId;

    Fabricante(int fabricanteId) {
        this.fabricanteId = fabricanteId;
    }

    public int getFabricanteId() {
        return fabricanteId;
    }
}
