package com.example.custom.repository;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;
import com.example.custom.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Long> {

    List<Arrival> findAllByDeclaration(Declaration declaration);
}
