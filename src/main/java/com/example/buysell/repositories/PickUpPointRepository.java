package com.example.buysell.repositories;

import com.example.buysell.models.PickUpPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickUpPointRepository extends JpaRepository<PickUpPoint, Long> {
    @Query("SELECT p FROM PickUpPoint p WHERE p.city.id = :cityId")
    List<PickUpPoint> findPickUpPointsByCityId(@Param("cityId") Long cityId);
}