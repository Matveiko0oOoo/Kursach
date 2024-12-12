package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.User;
import com.example.buysell.models.enums.Role;
import com.example.buysell.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user, Model model) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("errorMessage", "Пользователь с email: " + email + " уже существует");
            return false;
        }
        if (!isPasswordValid(user.getPassword())) {
            model.addAttribute("errorMessage", "Пароль должен быть длиннее 8 символов, содержать буквы и цифры, и не иметь 5 одинаковых символов подряд.");
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }


    public List<User> list(){
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()){
                user.setActive(false);
                log.info("Banning user with id: {}; email: {}", user.getId(), user.getEmail());
            }
            else {
                user.setActive(true);
                log.info("Un banning user with id: {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }


    public Optional<User> updateUserProfile(Long userId, String name, String numberPhone, MultipartFile avatarImage) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check for unique email
            // Removed for email since it's not being updated

            user.setName(name);
            user.setNumberPhone(numberPhone);

            // Handle image upload
            if (avatarImage != null && !avatarImage.isEmpty()) {
                Image image = new Image();
                image.setName(avatarImage.getOriginalFilename());
                image.setFileName(avatarImage.getOriginalFilename());
                image.setSize(avatarImage.getSize());
                image.setFileType(avatarImage.getContentType());
                try {
                    image.setBytes(avatarImage.getBytes());
                } catch (IOException e) {
                    log.error("Failed to upload image", e);
                    return Optional.empty();
                }
                user.setAvatarImage(image);
            }

            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email); // Предполагается, что у вас есть этот метод
    }

    public boolean isPasswordValid(String password) {
        if (password.length() < 8) return false; // Минимальная длина
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) return false; // Буквы и цифры
        if (password.matches(".*(.)\\1{4,}.*")) return false; // Не более 4 подряд одинаковых символов
        return true;
    }

}
