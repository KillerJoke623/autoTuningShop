package com.auto.data.services;

import com.auto.data.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Object[]> getMostPopularBrands() {
        return carRepository.findTopCarBrands();
    }

    public List<Object[]> getMostPopularModels() {
        return carRepository.findTopCarModels();
    }

    public List<Object[]> getCarsDistributionByClass() {
        return carRepository.findCarDistributionByClass();
    }
}
