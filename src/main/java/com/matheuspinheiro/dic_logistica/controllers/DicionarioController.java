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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dicionario")
@Validated
public class DicionarioController {

    @Autowired
    private DicionarioService dicionarioService;

    @GetMapping("/palavras")
    public ResponseEntity<List<Palavra>> obterTodasAsPalavras() {
        List<Palavra> objs = this.dicionarioService.findAll();
        return ResponseEntity.ok().body(objs);
    }

    @GetMapping("/palavras/{palavra}")
    public ResponseEntity<Palavra> obterPalavraPeloNome(@PathVariable String palavra) {
        Palavra obj = this.dicionarioService.findByPalavra(palavra);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/palavras")
    @Validated(CreatePalavra.class)
    public ResponseEntity<Void> create(@Valid @RequestBody Palavra obj) {
        this.dicionarioService.create(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/palavras/{id}")
    @Validated(UpdatePalavra.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Palavra obj, @PathVariable Long id) {
        obj.setId(id);
        this.dicionarioService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.dicionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
