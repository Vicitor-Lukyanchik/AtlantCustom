package com.example.custom.repository;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Declaration;
import com.example.custom.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    List<Consumption> findAllById(Long id);

    @Modifying
    @Query(value = "ALTER TABLE consumption AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
