package br.com.grupo27.tech.challenge.produto.model.serializer;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
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

class CategoriaSerializerTest {

    @InjectMocks
    private CategoriaSerializer categoriaSerializer;

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
    void testSerialize_withValidCategoria() throws IOException {
        var categoria = Categoria.MASCULINO;

        categoriaSerializer.serialize(categoria, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(categoria.getCategoriaId());
    }

    @Test
    void testSerialize_withNullCategoria() throws IOException {
        assertThrows(NullPointerException.class, () -> categoriaSerializer.serialize(null, jsonGenerator, serializerProvider));
    }
}