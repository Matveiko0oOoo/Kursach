package com.example.buysell.controllers;

import com.example.buysell.models.Image;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ImageRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getFileName())
                .contentType(MediaType.valueOf(image.getFileType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @GetMapping("/images/user/{userId}")
    public ResponseEntity<?> getUserAvatar(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getAvatarImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Изображение не найдено");
        }

        Image image = user.getAvatarImage(); // Получаем изображение из пользователя

        return ResponseEntity.ok()
                .header("fileName", image.getFileName())
                .contentType(MediaType.valueOf(image.getFileType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
