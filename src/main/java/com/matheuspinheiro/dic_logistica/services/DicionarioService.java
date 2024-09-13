package com.matheuspinheiro.dic_logistica.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.matheuspinheiro.dic_logistica.models.Palavra;
import com.matheuspinheiro.dic_logistica.models.enums.ProfileEnum;
import com.matheuspinheiro.dic_logistica.repositories.PalavraRepository;
import com.matheuspinheiro.dic_logistica.security.UserSpringSecurity;
import com.matheuspinheiro.dic_logistica.services.exceptions.AuthorizationException;
import com.matheuspinheiro.dic_logistica.services.exceptions.DataBindingViolationException;
import com.matheuspinheiro.dic_logistica.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class DicionarioService {

    @Autowired
    private PalavraRepository palavraRepository;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Palavra> findAll() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso Negado");
        }
        List<Palavra> palavras = this.palavraRepository.findAll();
        return palavras;
    }

    public Palavra findById(Long id) {
        Palavra palavra = this.palavraRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Palavra não encontrada! Id: " + id + ", Tipo: " + Palavra.class.getName()));
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN))
            throw new AuthorizationException("Acesso Negado!");
        return palavra;
    }

    public Palavra findByPalavra(String palavra) {
        Optional<Palavra> palavraResultado = this.palavraRepository.findByPalavra(palavra);
        return palavraResultado.orElseThrow(() -> new ObjectNotFoundException(
                "Palavra não encontrada! palavra: " + palavra + "tipo: " + Palavra.class.getName()));
    }

    @Transactional
    public Palavra create(Palavra obj) {
        obj.setId(null);
        obj = this.palavraRepository.save(obj);
        return obj;
    }

    @Transactional
    public Palavra update(Palavra obj) {
        Palavra newObj = findById(obj.getId());
        newObj.setSignificado(obj.getSignificado());
        newObj.setPalavra(newObj.getPalavra());
        return this.palavraRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.palavraRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("não é possivel deletar pois há entidades relacionadas");
        }
    }

}
