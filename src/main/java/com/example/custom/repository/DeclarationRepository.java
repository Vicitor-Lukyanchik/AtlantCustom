package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

    Optional<Declaration> findByIdCode(Long idCode);
    Optional<Declaration> findByNumber(String number);
}
