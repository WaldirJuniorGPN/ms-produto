package br.com.grupo27.tech.challenge.produto.model.deserializer;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import br.com.grupo27.tech.challenge.produto.model.Fabricante;
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

class FabricanteDeserializerTest {

    @InjectMocks
    private FabricanteDeserializer fabricanteDeserializer;

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
        int fabrincanteId = 1;
        when(jsonParser.getIntValue()).thenReturn(fabrincanteId);

        var result = fabricanteDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(Fabricante.fromId(fabrincanteId), result);
    }

    @Test
    void testDeserialize_withInvalidId() throws IOException {
        int invalidFabricanteId = -1;
        when(jsonParser.getIntValue()).thenReturn(invalidFabricanteId);

        assertThrows(IllegalArgumentException.class, () ->
                fabricanteDeserializer.deserialize(jsonParser, deserializationContext));
    }
}