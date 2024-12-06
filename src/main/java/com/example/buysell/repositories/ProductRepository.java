package com.example.buysell.repositories;

import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String Title);
    List<Product> findByCity(String City);
    List<Product> findByTitleAndCity(String title, String city);

    @Override
    void delete(Product entity);
}
