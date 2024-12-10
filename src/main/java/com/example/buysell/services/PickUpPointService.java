package com.example.buysell.services;

import com.example.buysell.models.PickUpPoint;
import com.example.buysell.repositories.PickUpPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PickUpPointService {
    private final PickUpPointRepository pickUpPointRepository;

    public List<PickUpPoint> getAllPickupPoints() {
        return pickUpPointRepository.findAll(); // Получаем все пункты выдачи
    }

    public List<PickUpPoint> getPickUpPointsByCity(Long cityId) {
        return pickUpPointRepository.findPickUpPointsByCityId(cityId);
    }

    public PickUpPoint savePickupPoint(PickUpPoint pickUpPoint) {
        return pickUpPointRepository.save(pickUpPoint);
    }

    public void deletePickupPointById(Long id) {
        pickUpPointRepository.deleteById(id);
    }

}