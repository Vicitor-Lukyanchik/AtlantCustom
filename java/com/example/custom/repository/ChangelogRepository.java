package com.example.custom.repository;

import com.example.custom.entity.Changelog;
import com.example.custom.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangelogRepository extends JpaRepository<Changelog, Long> {

     List<Changelog> findAllByName(String name);

     @Modifying
     @Query(value = "ALTER TABLE changelog AUTO_INCREMENT = 1", nativeQuery = true)
     void setAutoincrementOnStart();
}
