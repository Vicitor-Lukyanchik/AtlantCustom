package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> {

    List<Import> findAllByDeclaration(Declaration declaration);
}
