package br.com.grupo27.tech.challenge.produto.model;

import lombok.Getter;

@Getter
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

    public static Fabricante fromId(int id) {
        for (Fabricante fabricante : values()) {
            if (fabricante.getFabricanteId() == id) {
                return fabricante;
            }
        }
        throw new IllegalArgumentException("Fabricante inválido: " + id);
    }
}
