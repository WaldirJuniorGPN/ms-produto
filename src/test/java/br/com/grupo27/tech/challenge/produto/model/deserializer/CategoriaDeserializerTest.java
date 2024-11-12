package br.com.grupo27.tech.challenge.produto.model.deserializer;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CategoriaDeserializerTest {

    @InjectMocks
    private CategoriaDeserializer categoriaDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testDeserialize_withValidId() throws IOException {
        int categoriaId = 1;
        when(jsonParser.getIntValue()).thenReturn(categoriaId);

        var result = categoriaDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(Categoria.fromId(categoriaId), result);
    }

    @Test
    void testDeserialize_withInvalidId() throws IOException {
        int invalidCategoriaId = -1;
        when(jsonParser.getIntValue()).thenReturn(invalidCategoriaId);

        assertThrows(IllegalArgumentException.class, () ->
                categoriaDeserializer.deserialize(jsonParser, deserializationContext));
    }
}