package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pickup_point")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickUpPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pickUpPointID")
    private Long pickUpPointId;

    @ManyToOne
    @JoinColumn(name = "cityID")
    private City city; // Связь с городом

    @Column(name = "name", nullable = false, length = 100)
    private String name; // Новое поле для названия пункта выдачи

    @Column(name = "location", length = 100)
    private String location;


    // Связь с документами
    @OneToMany(mappedBy = "pickupPoint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Document> documents = new ArrayList<>();
}
