package br.com.grupo27.tech.challenge.produto.controller;

import br.com.grupo27.tech.challenge.produto.model.dto.request.ProdutoRequestDto;
import br.com.grupo27.tech.challenge.produto.model.dto.response.ProdutoResponseDto;
import br.com.grupo27.tech.challenge.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@RequestBody ProdutoRequestDto dto,
                                                        UriComponentsBuilder uriBuilder) {
        var produtoResponseDto = produtoService.cadastrar(dto);
        var uri = uriBuilder.path("/produtos/{id}")
                .buildAndExpand(produtoResponseDto.id()).toUri();

        return ResponseEntity.created(uri).body(produtoResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> buscarTodos(Pageable pageable){
        var produtoResponseDtos = produtoService.buscarTodos(pageable);

        return ResponseEntity.ok(produtoResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id){
        var produtoResponseDto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(produtoResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Integer id,
                                                        @RequestBody ProdutoRequestDto dto){

        var produtoResponseDto = produtoService.atualizar(id, dto);

        return ResponseEntity.ok(produtoResponseDto);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        produtoService.remover(id);
    }

    @PutMapping("/atualizar/estoque/{id}/{quantidade}")
    public ResponseEntity<ProdutoResponseDto> atualizarEstoue(@PathVariable Integer id, @PathVariable int quantidade){

        var produtoResponseDto = produtoService.atualizarEstoque(id, quantidade);

        return  ResponseEntity.ok(produtoResponseDto);
    }
}

