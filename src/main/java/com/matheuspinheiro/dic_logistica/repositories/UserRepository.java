package com.matheuspinheiro.dic_logistica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matheuspinheiro.dic_logistica.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User findByUsername(String username);

}
