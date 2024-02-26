package com.example.custom.repository;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Long> {

    Arrival findArrivalByProductName(String name);

    List<Arrival> findAllByDeclaration(Declaration declaration);

    @Query(value = "select arr  " +
            "from Arrival arr left join Consumption con on arr.id=con.arrival.id " +
            "group by arr.id having arr.turnoverCount > ROUND(SUM(con.turnoverCount), 4) or ROUND(SUM(con.turnoverCount), 4) is null")
    List<Arrival> findNotReadyArrivals();

    @Modifying
    @Query(value = "ALTER TABLE arrival AUTO_INCREMENT = 1", nativeQuery = true)
    void setAutoincrementOnStart();
}
