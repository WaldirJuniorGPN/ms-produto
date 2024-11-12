package br.com.grupo27.tech.challenge.produto.model.serializer;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class FabricanteSerializer extends JsonSerializer<Fabricante> {

    @Override
    public void serialize(Fabricante fabricante, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(fabricante.getFabricanteId());
    }
}
