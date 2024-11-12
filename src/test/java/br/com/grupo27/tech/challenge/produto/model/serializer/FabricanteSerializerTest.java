package br.com.grupo27.tech.challenge.produto.model.serializer;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class FabricanteSerializerTest {

    @InjectMocks
    private FabricanteSerializer fabricanteSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

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
    void testSerialize_withValidFabricante() throws IOException {
        var fabricante = Fabricante.NIKE;

        fabricanteSerializer.serialize(fabricante, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(fabricante.getFabricanteId());
    }

    @Test
    void testSerialize_withNullFabricante() throws IOException {
        assertThrows(NullPointerException.class, () -> fabricanteSerializer.serialize(null, jsonGenerator, serializerProvider));
    }
}