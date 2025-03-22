package com.auto.data.models;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer car_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "users_user_id", nullable = false)
    private Users users;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturers_manufacturer_id", nullable = false)
    private Manufacturers manufacturers;

    @ManyToOne(optional = false)
    @JoinColumn(name = "model_model_id", nullable = false)
    private Model model;

    @Column(name = "is_active") // Добавляем поле is_active и мапим на столбец is_active
    private Boolean isActive = true; // По умолчанию автомобиль активен
}
