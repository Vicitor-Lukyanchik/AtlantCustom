package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.Export;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportRepository extends JpaRepository<Export, Long> {

    List<Export> findAllByDeclaration(Declaration declaration);
}
