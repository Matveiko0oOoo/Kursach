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

    public List<Product> listProducts(String title, String city, String sortOrder) {
        List<Product> products = title != null && !title.isEmpty()
                ? productRepository.findByTitleContainingIgnoreCase(title)
                : productRepository.findAll();

        // Фильтрация по городу
        if (city != null && !city.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getCity() != null && product.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }

        // Сортировка
        if (sortOrder != null) {
            switch (sortOrder) {
                case "priceAsc":
                    products.sort(Comparator.comparing(Product::getPrice));
                    break;
                case "priceDesc":
                    products.sort(Comparator.comparing(Product::getPrice).reversed());
                    break;
                case "nameAsc":
                    products.sort(Comparator.comparing(Product::getTitle));
                    break;
                case "nameDesc":
                    products.sort(Comparator.comparing(Product::getTitle).reversed());
                    break;
            }
        }

        return products;
    }

    // Сохранение продукта с изображениями
    @Transactional
    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));

        // Удаляем старые изображения, если они есть
        if (!product.getImages().isEmpty()) {
            imageRepository.deleteAll(product.getImages());
            product.getImages().clear();
        }

        List<Image> newImages = new ArrayList<>();
        addImageIfPresent(file1, newImages);
        addImageIfPresent(file2, newImages);
        addImageIfPresent(file3, newImages);

        // Привязываем новые изображения к продукту
        for (Image image : newImages) {
            image.setProduct(product);
        }
        product.getImages().addAll(newImages);

        // Сохраняем продукт без mainImageId, чтобы изображения получили свои ID
        productRepository.save(product);

        // Устанавливаем первое изображение как главное
        if (!newImages.isEmpty()) {
            Image mainImage = newImages.get(0);
            mainImage.setMainImage(true);
            imageRepository.saveAll(newImages);
            product.setMainImageId(mainImage.getId());
        }

        log.info("Saving product. Title: {}; Images: {}", product.getTitle(), product.getImages().size());
        productRepository.save(product); // Повторное сохранение с обновлённым mainImageId
    }

    // Получение пользователя по Principal
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }

    // Преобразование MultipartFile в Image
    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    // Удаление продукта по ID
    public void deleteProduct(Long id) {
        imageRepository.deleteByProductId(id); // Удаляем связанные изображения
        productRepository.deleteById(id); // Удаляем сам продукт
        log.info("Продукт с ID {} успешно удалён.", id);
    }

    // Поиск продуктов по списку ID
    public List<Product> findProductsByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    // Получение продукта по ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Исправление сиротских изображений
    public void fixOrphanImages(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            log.error("Продукт с id {} не найден", productId);
            return;
        }

        List<Image> productImages = product.getImages();
        List<Image> allImages = imageRepository.findAll();

        for (Image image : allImages) {
            if (image.getProduct() == null || !productImages.contains(image)) {
                imageRepository.delete(image);
                log.info("Сиротское изображение с id {} удалено", image.getId());
            }
        }
    }

    // Метод для добавления изображения, если файл присутствует
    private void addImageIfPresent(MultipartFile file, List<Image> newImages) throws IOException {
        if (file != null && file.getSize() > 0) {
            newImages.add(toImageEntity(file));
        }
    }

    // Метод для поиска продукта по ID
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден с id: " + productId));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Получаем все продукты
    }
}