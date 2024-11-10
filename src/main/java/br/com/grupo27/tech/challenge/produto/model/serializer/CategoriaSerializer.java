package br.com.grupo27.tech.challenge.produto.model.serializer;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CategoriaSerializer extends JsonSerializer<Categoria> {

    @Override
    public void serialize(Categoria categoria, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(categoria.getCategoriaId());
    }
}
