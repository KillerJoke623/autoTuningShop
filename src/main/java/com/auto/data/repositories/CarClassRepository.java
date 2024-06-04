package com.auto.data.repositories;

import com.auto.data.models.CarClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarClassRepository extends JpaRepository<CarClass, Integer> {
}
