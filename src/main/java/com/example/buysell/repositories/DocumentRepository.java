package com.example.buysell.repositories;

import com.example.buysell.models.Document;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findFirstByBusinessCardOrderByCreatedDateDesc(User user);
}