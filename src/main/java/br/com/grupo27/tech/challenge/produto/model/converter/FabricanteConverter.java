package br.com.grupo27.tech.challenge.produto.model.converter;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FabricanteConverter implements AttributeConverter<Fabricante, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Fabricante fabricante) {
        return fabricante != null ? fabricante.getFabricanteId() : null;
    }

    @Override
    public Fabricante convertToEntityAttribute(Integer fabricanteId) {
        return fabricanteId != null ? Fabricante.fromId(fabricanteId) : null;
    }
}
