package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.Export;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportRepository extends JpaRepository<Export, Long> {

    List<Export> findAllByDeclaration(Declaration declaration);

    @Modifying
    @Query(value = "ALTER TABLE export AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
