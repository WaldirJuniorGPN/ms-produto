package br.com.grupo27.tech.challenge.produto.config;

import br.com.grupo27.tech.challenge.produto.model.Produto;
import br.com.grupo27.tech.challenge.produto.utils.ProdutoProcessor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ImpotacaoJobConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;



    @Bean
    public Job job(Step step, JobRepository jobRepository) {
        return new JobBuilder("Importacao-produto", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .next(moverArquivosStep(jobRepository))
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
                .names("nome","descricao","sku","preco","quantidadeEstoque","peso")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Produto>()
                .dataSource(dataSource)
                .sql("INSERT INTO tb_produto"
                               +"(nome,descricao,sku,preco,quantidade_estoque,peso,status)"
                 +"values(:nome,:descricao,:sku,:preco,:quantidadeEstoque,:peso, :status)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
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

                        LocalDate dataHoje = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // Formato AAAAMMDD
                        String dataFormatada = dataHoje.format(formatter);
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
