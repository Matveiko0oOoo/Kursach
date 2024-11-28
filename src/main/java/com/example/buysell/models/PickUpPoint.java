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

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "stars")
    private Integer stars;

    // Связь с документами
    @OneToMany(mappedBy = "pickupPoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();
}
