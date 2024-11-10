package br.com.grupo27.tech.challenge.produto.model.converter;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoriaConverter implements AttributeConverter<Categoria, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Categoria categoria) {
        return categoria != null ? categoria.getCategoriaId() : null;
    }

    @Override
    public Categoria convertToEntityAttribute(Integer categoriaId) {
        return categoriaId != null ? Categoria.fromId(categoriaId) : null;
    }
}
