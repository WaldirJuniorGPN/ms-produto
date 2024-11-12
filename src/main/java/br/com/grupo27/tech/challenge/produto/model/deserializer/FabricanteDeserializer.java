package br.com.grupo27.tech.challenge.produto.model.deserializer;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class FabricanteDeserializer extends JsonDeserializer<Fabricante> {

    @Override
    public Fabricante deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var fabricanteId = jsonParser.getIntValue();
        return Fabricante.fromId(fabricanteId);
    }
}
