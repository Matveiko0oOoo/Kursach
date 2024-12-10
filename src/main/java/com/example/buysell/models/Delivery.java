package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "delivery_name")
    private String deliveryName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "arrival_date")
    private String arrivalDate;

    @Column(name = "admission_date")
    private String admissionDate;

    @Column(name = "place_in_stock", length = 30)
    private String placeInStock;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "delivery_product_ids", joinColumns = @JoinColumn(name = "delivery_id"))
    @Column(name = "product_id")
    private List<Long> productIds = new ArrayList<>(); // Хранение идентификаторов товаров

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "pickup_point_id")
    private PickUpPoint pickUpPoint;
}