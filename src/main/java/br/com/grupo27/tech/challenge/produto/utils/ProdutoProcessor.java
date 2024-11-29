package br.com.grupo27.tech.challenge.produto.utils;

import br.com.grupo27.tech.challenge.produto.model.Categoria;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.server.DelegatingServerHttpResponse;


public class ProdutoProcessor implements ItemProcessor<Produto, Produto> {
    @Override
    public Produto process(Produto item) throws Exception {

        item.setStatus(true);


        return item;
    }
}
