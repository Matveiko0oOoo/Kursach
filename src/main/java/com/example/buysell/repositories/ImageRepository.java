package com.example.buysell.repositories;

import com.example.buysell.models.Image;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAll(@Nullable Iterable<? extends Image> entities);
    List<Image> findByProductIsNull();
}

