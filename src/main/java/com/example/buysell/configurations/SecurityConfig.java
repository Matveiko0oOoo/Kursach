package com.example.buysell.configurations;

import com.example.buysell.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/product/**", "/login", "/registration", "/images/**", "/user/**", "/static/**", "/profile", "/image/avatar.png", "/image/cart.png","/image/CART_onProduct.png", "/cart/**")
                        .permitAll() // Разрешить доступ к страницам входа и регистрации
                        .requestMatchers("/admin/**", "/product/delete/**").hasRole("ADMIN") // Доступ только для администраторов
                        .requestMatchers("/product/create", "/profile/edit/**").authenticated() // Требовать аутентификации для создания товара
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(customizer -> customizer
                        .loginPage("/login") // Укажите URL для страницы входа
                        .failureUrl("/login?error=true") // Укажите URL для обработки ошибок
                        .permitAll() // Разрешить доступ к странице входа
                )
                .logout(logout -> logout
                        .permitAll()); // Разрешить доступ к выходу
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}