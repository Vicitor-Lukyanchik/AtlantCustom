package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

    Optional<Declaration> findByIdCode(Long idCode);
    Optional<Declaration> findByNumber(String number);

    @Modifying
    @Query(value = "ALTER TABLE declaration AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
