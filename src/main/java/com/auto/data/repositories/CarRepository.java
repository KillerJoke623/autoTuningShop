package com.auto.data.repositories;

import com.auto.data.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT c.manufacturers.manufacturer, COUNT(c) FROM Car c GROUP BY c.manufacturers.manufacturer ORDER BY COUNT(c) DESC")
    List<Object[]> findTopCarBrands();

    @Query("SELECT c.manufacturers.manufacturer, c.model.model_name, COUNT(c) FROM Car c GROUP BY c.manufacturers.manufacturer, c.model.model_name ORDER BY COUNT(c) DESC")
    List<Object[]> findTopCarModels();

    @Query("SELECT c.model.carClass.name, COUNT(c) FROM Car c GROUP BY c.model.carClass.name")
    List<Object[]> findCarDistributionByClass();
}