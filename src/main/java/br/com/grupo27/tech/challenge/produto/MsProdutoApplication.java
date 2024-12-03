package br.com.grupo27.tech.challenge.produto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MsProdutoApplication {



    public static void main(String[] args) {
        SpringApplication.run(MsProdutoApplication.class, args);
    }

}
