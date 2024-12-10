package com.example.buysell.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
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

    // Дополнительные поля для работы с документом
    @Transient
    private InputStream inputStream; // Поток для скачивания

    @Transient
    private String filename; // Имя файла

    @Transient
    private String contentType; // Тип контента (например, "application/pdf")

    // Методы для получения данных о документе
    public String getFilename() {
        return documentID + ".pdf"; // Пример для PDF, можно адаптировать под разные форматы
    }

    public String getContentType() {
        return "application/pdf"; // Пример для PDF, можно адаптировать под разные форматы
    }

    public InputStream getInputStream() {
        // Логика для получения InputStream на основе content или других данных
        // Например, можно генерировать PDF на лету
        return null; // Замените на реальную логику
    }
}