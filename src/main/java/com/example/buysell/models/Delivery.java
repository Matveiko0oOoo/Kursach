package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryID")
    private Long deliveryId;

    @Column(name = "deliveryName")
    private String deliveryName;

    @ManyToOne
    @JoinColumn(name = "cityID")
    private City city;

    @Column(name = "arrivalDate")
    private LocalDate arrivalDate;

    @Column(name = "admissionDate")
    private LocalDate admissionDate;

    @Column(name = "placeInStock", length = 30)
    private String placeInStock;

    // Связь с таблицей Document
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Document> documents = new ArrayList<>();
}
