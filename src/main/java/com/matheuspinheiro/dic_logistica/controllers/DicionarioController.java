package com.matheuspinheiro.dic_logistica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import com.matheuspinheiro.dic_logistica.models.Palavra;
import com.matheuspinheiro.dic_logistica.models.Palavra.CreatePalavra;
import com.matheuspinheiro.dic_logistica.models.Palavra.UpdatePalavra;
import com.matheuspinheiro.dic_logistica.services.DicionarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/dicionario")
@Validated
@Tag(name = "Dicionário TMS", description = "Operações com as palavras")
public class DicionarioController {

    @Autowired
    private DicionarioService dicionarioService;

    @Operation(summary = "Find All", description = "Busca uma lista com todas as palavras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista com o dicionário inteiro"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado, acesso negado") })
    @GetMapping("/palavras")
    public ResponseEntity<List<Palavra>> obterTodasAsPalavras() {
        List<Palavra> objs = this.dicionarioService.findAll();
        return ResponseEntity.ok().body(objs);
    }

    @Operation(summary = "Find By Name", description = "Busca uma palavra pelo seu nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a palavra, seu significado e seu id"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado, acesso negado"),
            @ApiResponse(responseCode = "404", description = "Palavra não encontrada") })
    @GetMapping("/palavras/{palavra}")
    public ResponseEntity<Palavra> obterPalavraPeloNome(@PathVariable String palavra) {
        Palavra obj = this.dicionarioService.findByPalavra(palavra);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Create Palavra", description = "Cria uma nova palavra no dicionário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Palavra criado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado, acesso negado"),
            @ApiResponse(responseCode = "409", description = "Conflito ao salvar a palavra"),
            @ApiResponse(responseCode = "422", description = "Não foi possível salvar a palavra"),
            @ApiResponse(responseCode = "500", description = "Erro interno na aplicação, tente novamente mais tarde") })
    @PostMapping("/palavras")
    @Validated(CreatePalavra.class)
    public ResponseEntity<Void> create(@Valid @RequestBody Palavra obj) {
        this.dicionarioService.create(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Change Significado", description = "Muda o significado de uma palavra, mudança feita pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Significado da palavra alterado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado, acesso negado"),
            @ApiResponse(responseCode = "404", description = "Palavra não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito ao salvar a palavra"),
            @ApiResponse(responseCode = "422", description = "Não foi possível salvar a palavra") })
    @PutMapping("/palavras/{id}")
    @Validated(UpdatePalavra.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Palavra obj, @PathVariable Long id) {
        obj.setId(id);
        this.dicionarioService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Palavra", description = "Deleta uma palavra pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Palavra deletada do banco de dados"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado, acesso negado"),
            @ApiResponse(responseCode = "404", description = "Palavra não encontrada"),
            @ApiResponse(responseCode = "409", description = "Não é possível deletar a palavra pois há entidades relacionadas") })
    @DeleteMapping("/palavras/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.dicionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
