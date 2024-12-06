package com.example.buysell.repositories;

import com.example.buysell.models.PickUpPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickUpPointRepository extends JpaRepository<PickUpPoint, Long> {
    @Query(value = "SELECT p FROM PickUpPoint p ORDER BY RAND() LIMIT 1")
    PickUpPoint findRandomPickUpPoint();
}