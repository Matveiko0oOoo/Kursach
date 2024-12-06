package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cityID")
    private City city;

    @ManyToOne
    @JoinColumn(name = "presentID")
    private Product present;

    @ManyToOne
    @JoinColumn(name = "deliveryID")
    private Delivery delivery;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    @Column(name = "documentID", length = 18)
    private String documentID;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "businessCardID")
    private User businessCard;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pickUPPointID")
    private PickUpPoint pickupPoint;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "imageID")
    private Image image;

    @Lob
    @Column(name = "content")
    private String content;
}
