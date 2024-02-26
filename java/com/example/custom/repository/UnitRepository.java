package com.example.custom.repository;

import com.example.custom.entity.Unit;
import com.example.custom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Modifying
    @Query(value = "ALTER TABLE unit AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
