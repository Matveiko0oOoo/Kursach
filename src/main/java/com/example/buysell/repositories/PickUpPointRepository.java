package com.example.buysell.repositories;

import com.example.buysell.models.PickUpPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickUpPointRepository extends JpaRepository<PickUpPoint, Long> {
    PickUpPoint findByLocation(String location);
}