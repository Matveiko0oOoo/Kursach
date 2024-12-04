package com.example.buysell.services;

import com.example.buysell.models.City;
import com.example.buysell.repositories.CityRepository; // Импортируйте репозиторий
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Город с ID " + id + " не найден."));
    }

    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }

}