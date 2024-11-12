package br.com.grupo27.tech.challenge.produto.model.deserializer;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CategoriaDeserializer extends JsonDeserializer<Categoria> {

    @Override
    public Categoria deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var categoriaId = jsonParser.getIntValue();
        return Categoria.fromId(categoriaId);
    }
}
