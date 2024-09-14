package com.matheuspinheiro.dic_logistica.controllers;

import java.net.URI;

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

import com.matheuspinheiro.dic_logistica.models.User;
import com.matheuspinheiro.dic_logistica.models.DTO.UserCreateDTO;
import com.matheuspinheiro.dic_logistica.models.DTO.UserUpdateDTO;
import com.matheuspinheiro.dic_logistica.services.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/")
@Validated
@OpenAPIDefinition(info = @Info(title = "API - Dicionário de Logística TMS", version = "1.0", description = "Uma API feita como dicionário para termos de logística", license = @License(name = "Apache 2.0")))
@Tag(name = "User", description = "Operações com User")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get Users", description = "Busca o usuário pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso Negado") })
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = this.userService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Create Users", description = "Cria um usuário novo no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito ao salvar as entidades") })
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) {
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Change Password", description = "Muda a senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "A senha do usuário foi alterada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Houve conflito ao tentar salvar a senha") })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id) {
        obj.setId(id);
        User user = this.userService.fromDTO(obj);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete User", description = "Deleta o usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "O usuário foi deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não existe ou não foi encontrado"),
            @ApiResponse(responseCode = "409", description = "Não é possível deletar o usuário pois há entidades relacionadas") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
