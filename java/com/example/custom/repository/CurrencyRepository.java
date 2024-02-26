package com.example.custom.repository;

import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Modifying
    @Query(value = "ALTER TABLE currency AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
