package br.com.grupo27.tech.challenge.produto.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateUtils {

    public static <T> void atualizarCampo(Supplier<T> getter, Consumer<T> setter, T novoValor) {
        if (!Objects.equals(getter.get(), novoValor)) {
            setter.accept(novoValor);
        }
    }
}
