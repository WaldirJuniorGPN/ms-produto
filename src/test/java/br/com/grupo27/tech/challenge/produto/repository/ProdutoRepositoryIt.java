package br.com.grupo27.tech.challenge.produto.repository;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto;
import static br.com.grupo27.tech.challenge.produto.mock.ProdutoDados.getProduto2;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class ProdutoRepositoryIt {

    @Autowired
    private ProdutoRepository repository;


    @Test
    public void deverPermitirCriarTabela(){
        var totalDeRegistros = repository.count();
        assertThat(totalDeRegistros).isNotNegative();
    }

    @Test
    void deverPermitirCriarProduto() {
        //Arrange
        var produto = getProduto();

        //Act
        var result = repository.save(produto);

        //Arrange
        assertThat(result)
                .isInstanceOf(Produto.class)
                .isNotNull();

       // assertThat(result.getId()).isEqualTo(produto.getId());
        assertThat(result.getNome()).isEqualTo(produto.getNome());
        assertThat(result.getDescricao()).isEqualTo(produto.getDescricao());
    }

    @Test
    void deverPermitirBuscarProduto() {
        //Arrenge
        var produto = cadastrarProduto(getProduto());
        var id = produto.getId();

        //Act
        var result = repository.findById(id);

        //Arrange
        assertThat(result)
                .isPresent()
                .containsSame(produto);

        result.ifPresent(
                produtoAmazenado -> {
                    assertThat(produtoAmazenado.getId()).isEqualTo(id);
                    assertThat(produtoAmazenado.getNome()).isEqualTo(produto.getNome());
                    assertThat(produtoAmazenado.getDescricao()).isEqualTo(produto.getDescricao());
                }
        );
    }

    @Test
    void deverPermitirRemoverProduto() {
        //Arrange
        var produto = cadastrarProduto(getProduto());
        var id = produto.getId();

        //Act
        repository.deleteById(id);
        var result = repository.findById(id);

        //Assert
        assertThat(result).isEmpty();
    }

    @Test
    void deverPermitirListarProdutos() {
        //Arrange
        var produto1 = cadastrarProduto(getProduto());
        var produto2 = cadastrarProduto(getProduto2());

        //Act
        var result = repository.findAll();

        //Arrange
        assertThat(result).hasSizeGreaterThan(0);
    }

    private Produto cadastrarProduto(Produto produto){
        var produtoAInserir = produto;
        return repository.save(produtoAInserir);
    }
}
