package com.example.custom.repository;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    List<Consumption> findAllByDeclaration(Declaration declaration);
}
