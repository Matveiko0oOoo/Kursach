package com.example.buysell.services;

import com.example.buysell.models.*;
import com.example.buysell.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final DocumentRepository documentRepository;


    // В ProductService
    public List<Product> listProducts(String title, String city, String sortOrder) {
        List<Product> products;

        // Поиск по названию
        if (title != null && !title.isEmpty()) {
            products = productRepository.findByTitleContainingIgnoreCase(title);
        } else {
            products = productRepository.findAll();
        }

        // Фильтрация по городу
        if (city != null && !city.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList()); // Используем Collectors.toList()
        }

        // Сортировка
        if ("priceAsc".equals(sortOrder)) {
            products.sort(Comparator.comparing(Product::getPrice));
        } else if ("priceDesc".equals(sortOrder)) {
            products.sort(Comparator.comparing(Product::getPrice).reversed());
        } else if ("nameAsc".equals(sortOrder)) {
            products.sort(Comparator.comparing(Product::getTitle));
        } else if ("nameDesc".equals(sortOrder)) {
            products.sort(Comparator.comparing(Product::getTitle).reversed());
        }

        return products;
    }


    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));

        // Если у продукта уже есть изображения, удаляем их перед добавлением новых
        if (!product.getImages().isEmpty()) {
            imageRepository.deleteAll(product.getImages());
            product.getImages().clear();
        }

        List<Image> newImages = new ArrayList<>();
        if (file1 != null && file1.getSize() > 0) {
            newImages.add(toImageEntity(file1));
        }
        if (file2 != null && file2.getSize() > 0) {
            newImages.add(toImageEntity(file2));
        }
        if (file3 != null && file3.getSize() > 0) {
            newImages.add(toImageEntity(file3));
        }

        // Привязываем новые изображения
        if (!newImages.isEmpty()) {
            for (Image image : newImages) {
                image.setProduct(product);
            }
            product.getImages().addAll(newImages);

            // Сохраняем продукт без mainImageId, чтобы изображения получили свои ID
            productRepository.save(product);

            // Устанавливаем первое изображение как главное
            Image mainImage = newImages.get(0);
            mainImage.setMainImage(true);
            imageRepository.saveAll(newImages); // Сохраняем новые изображения с привязкой к продукту

            product.setMainImageId(mainImage.getId());
        }

        log.info("Saving product. Title: {}; Images: {}", product.getTitle(), product.getImages().size());
        productRepository.save(product); // Повторное сохранение с обновлённым mainImageId
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            // Удаляем связанные изображения
            List<Image> images = product.getImages();
            if (!images.isEmpty()) {
                imageRepository.deleteAll(images); // Удаление всех изображений за один раз
            }

            // Удаляем документы, связанные с продуктом
            List<Document> documents = product.getDocuments();
            if (!documents.isEmpty()) {
                documentRepository.deleteAll(documents); // Удаление всех документов за один раз
            }

            // Удаляем сам продукт
            productRepository.delete(product);
            log.info("Товар с id {} и все его связи удалены", id);
        } else {
            log.warn("Товар с id {} не найден", id);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void fixOrphanImages(Long productId) {
        // Получаем продукт по ID
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            log.error("Product with id {} not found", productId);
            return;
        }

        // Получаем все изображения, связанные с продуктом
        List<Image> productImages = product.getImages();

        // Получаем все изображения из базы данных
        List<Image> allImages = imageRepository.findAll();

        // Находим изображения, которые не связаны с продуктом
        for (Image image : allImages) {
            if (image.getProduct() == null || !productImages.contains(image)) {
                // Если изображение не связано с продуктом, удаляем его
                imageRepository.delete(image);
                log.info("Orphan image with id {} deleted", image.getId());
            }
        }
    }

}
