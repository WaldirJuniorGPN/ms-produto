package br.com.grupo27.tech.challenge.produto.utils;

import br.com.grupo27.tech.challenge.produto.model.Fabricante;
import br.com.grupo27.tech.challenge.produto.model.Produto;
import org.springframework.batch.item.ItemProcessor;

public class ProdutoProcessor implements ItemProcessor<Produto, Produto> {
    @Override
    public Produto process(Produto item) throws Exception {

        item.setStatus(true);
        return item;
    }
}
