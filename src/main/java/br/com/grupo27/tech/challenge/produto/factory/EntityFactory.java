package br.com.grupo27.tech.challenge.produto.factory;

public interface EntityFactory<T, D extends Record> {

    T criar(D dto);

    void atualizar(T classe, D dto);
}
