package br.com.grupo27.tech.challenge.produto.model.converter;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoriaConverterTest {

    private CategoriaConverter categoriaConverter;

    @BeforeEach
    void setup() {
        categoriaConverter = new CategoriaConverter();
    }

    @Test
    void testConvertToDatabaseColumn_withValidCategoria() {
        var categoria = Categoria.MASCULINO;
        var result = categoriaConverter.convertToDatabaseColumn(categoria);

        assertEquals(categoria.getCategoriaId(), result);
    }

    @Test
    void testConvertToDatabaseColumn_withNullCategoria() {
        var result = categoriaConverter.convertToDatabaseColumn(null);

        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withValidId() {
        var categoriaId = 1;
        var result = categoriaConverter.convertToEntityAttribute(categoriaId);

        assertEquals(Categoria.fromId(categoriaId), result);
    }

    @Test
    void testConvertToEntityAttribute_withNullId() {
        var result = categoriaConverter.convertToEntityAttribute(null);

        assertNull(result);
    }
}