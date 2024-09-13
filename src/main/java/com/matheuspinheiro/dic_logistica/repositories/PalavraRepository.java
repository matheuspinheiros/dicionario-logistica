package com.matheuspinheiro.dic_logistica.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheuspinheiro.dic_logistica.models.Palavra;

@Repository
public interface PalavraRepository extends JpaRepository<Palavra, Long> {
    List<Palavra> findAll();

    Optional<Palavra> findByPalavra(String palavra);
}
