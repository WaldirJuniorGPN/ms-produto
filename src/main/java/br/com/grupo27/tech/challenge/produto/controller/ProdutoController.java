package br.com.grupo27.tech.challenge.produto.controller;

import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@RequestBody @Valid ProdutoRequestDto dto,
                                                        UriComponentsBuilder uriBuilder) {
        var produtoResponseDto = produtoService.cadastrar(dto);
        var uri = uriBuilder.path("/produtos/{id}")
                .buildAndExpand(produtoResponseDto.id()).toUri();

        return ResponseEntity.created(uri).body(produtoResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> buscarTodos(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        var produtoResponseDtos = produtoService.buscarTodos(pageable);

        return ResponseEntity.ok(produtoResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Long id) {
        var produtoResponseDto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(produtoResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Long id,
                                                        @RequestBody ProdutoRequestDto dto) {

        var produtoResponseDto = produtoService.atualizar(id, dto);

        return ResponseEntity.ok(produtoResponseDto);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.remover(id);
    }


    @PatchMapping("/atualizar/estoque/{id}/{quantidade}")
    public ResponseEntity<ProdutoResponseDto> atualizarEstoue(@PathVariable Long id, @PathVariable Integer quantidade) {

        var produtoResponseDto = produtoService.atualizarEstoque(id, quantidade);

        return ResponseEntity.ok(produtoResponseDto);
    }

    @PostMapping("/importacao")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile arquivo) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        try {
            produtoService.uploadArquivoCsv(arquivo);
            produtoService.startJob();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao importar arquivo");
        }

        return ResponseEntity.ok("Arquivo importado com sucesso");
    }
}

