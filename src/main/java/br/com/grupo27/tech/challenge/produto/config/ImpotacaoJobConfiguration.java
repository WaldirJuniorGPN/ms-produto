package br.com.grupo27.tech.challenge.produto.config;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.utils.ProdutoProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ImpotacaoJobConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;



    @Bean
    public Job job(Step step,
                   Step moverArquivosStep,
                   JobRepository jobRepository) {
        return new JobBuilder("Importacao-produto", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .next(moverArquivosStep)
                .build();
    }

    @Bean
    public Step step(ItemReader<Produto> itemReader,
                     ItemWriter<Produto> itemWriter,
                     ItemProcessor<Produto, Produto> itemProcessor,
                     JobRepository jobRepository) {
        return new StepBuilder("step", jobRepository)
                .<Produto, Produto>chunk(20, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ItemReader<Produto> itemReader() {

        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Produto.class);


        return new FlatFileItemReaderBuilder<Produto>()
                .name("leitura-csv")
                .resource(new FileSystemResource("src\\main\\resources\\produtos.csv"))
                .comments("--")
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names("nome","descricao","sku","preco","quantidadeEstoque","peso","categoriaId",
                        "fabricanteId", "imagemPrincipalUrl",  "imagensAdicionaisUrl",
                        "tags","urlAmigavel","metaTitle","metaDescrition")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Produto>()
                .dataSource(dataSource)
                .sql("INSERT INTO tb_produto"
                               +"(nome,descricao,sku,preco,quantidade_estoque,peso, categoria_id, fabricante_id, imagem_principal_url,  imagens_adicionais_url, tags, url_amigavel, meta_title, meta_descrition, status)"
                 +"values(:nome,:descricao,:sku,:preco,:quantidadeEstoque,:peso, :categoriaId, :fabricanteId, :imagemPrincipalUrl, :imagensAdicionaisUrl, :tags, :urlAmigavel, :metaTitle, :metaDescrition,   :status)")
                .itemSqlParameterSourceProvider(itemSqlParameterSourceProvider())
                .build();
    }

    @Bean
    public ItemSqlParameterSourceProvider<Produto> itemSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<Produto>() {
            @Override
            public SqlParameterSource createSqlParameterSource(Produto produto) {
                MapSqlParameterSource parameterSource = new MapSqlParameterSource();
                parameterSource.addValue("nome", produto.getNome());
                parameterSource.addValue("descricao", produto.getDescricao());
                parameterSource.addValue("sku", produto.getSku());
                parameterSource.addValue("preco", produto.getPreco());
                parameterSource.addValue("quantidadeEstoque", produto.getQuantidadeEstoque());
                parameterSource.addValue("peso", produto.getPeso());
                parameterSource.addValue("categoriaId", produto.getCategoriaId().getCategoriaId()); // Converte enum para String
                parameterSource.addValue("fabricanteId", produto.getFabricanteId().getFabricanteId());
                parameterSource.addValue("imagemPrincipalUrl", produto.getImagemPrincipalUrl());
                parameterSource.addValue("imagensAdicionaisUrl", produto.getImagensAdicionaisUrl());
                parameterSource.addValue("tags", produto.getTags());
                parameterSource.addValue("urlAmigavel", produto.getUrlAmigavel());
                parameterSource.addValue("metaTitle", produto.getMetaTitle());
                parameterSource.addValue("metaDescrition", produto.getMetaDescrition());
                parameterSource.addValue("status", produto.isStatus());
                return parameterSource;
            }
        };
    }


    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();
    }

    @Bean
    public Tasklet moverArquivos(){
        return (contribution, chunkContext) -> {
            File pastaOrigem = new File("src\\main\\resources");
            File pastaDestino = new File("src\\main\\resources\\arquivos-importados");

            if(!pastaDestino.exists()){
                pastaDestino.mkdirs();
            }

            File[] arquivos = pastaOrigem.listFiles((dir, name) -> name.endsWith(".csv"));

            if(arquivos != null ){
                for (File arquivo : arquivos){


                    File arquivoDestino = new File(pastaDestino, arquivo.getName());

                    if(arquivo.renameTo(arquivoDestino)){

                        System.out.println("Arquivo movido: " + arquivo.getName());

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss"); // Formato AAAAMMDD
                        String dataFormatada = LocalDateTime.now().format(formatter);
                        String novoNomeArquivo = "produtos_" + dataFormatada + ".csv";
                        File novoArquivo = new File(arquivoDestino.getParent(), novoNomeArquivo);
                        arquivoDestino.renameTo(novoArquivo);
                    }else{
                        throw new RuntimeException("Não foi possível mover o arquivo.");
                    }

                }

            }
            return RepeatStatus.FINISHED;
        };

    }

    @Bean
    public Step moverArquivosStep(JobRepository jobRepository) {
        return new StepBuilder("mover-arquivo", jobRepository)
                .tasklet(moverArquivos(), transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
