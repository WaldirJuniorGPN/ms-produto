package br.com.grupo27.tech.challenge.produto.model.converter;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FabricanteConverterTest {

    private FabricanteConverter fabricanteConverter;

    @BeforeEach
    void setup() {
        fabricanteConverter = new FabricanteConverter();
    }

    @Test
    void testConvertToDatabaseColumn_withValidFabricante() {
        var fabricante = Fabricante.NIKE;
        var result = fabricanteConverter.convertToDatabaseColumn(fabricante);

        assertEquals(fabricante.getFabricanteId(), result);
    }

    @Test
    void testConvertToDatabaseColumn_withNullFabricante() {
        var result = fabricanteConverter.convertToDatabaseColumn(null);

        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withValidId() {
        var fabricanteId = 1;
        var result = fabricanteConverter.convertToEntityAttribute(fabricanteId);

        assertEquals(Fabricante.fromId(fabricanteId), result);
    }

    @Test
    void testConvertToEntityAttribute_withNullId() {
        var result = fabricanteConverter.convertToEntityAttribute(null);

        assertNull(result);
    }
}