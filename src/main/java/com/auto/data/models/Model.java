package com.auto.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long model_id;
    private String model_name;


    private Integer generation;



    @ManyToOne
    @JoinColumn(name = "manufacturer_manufacturer_id")
    private Manufacturers manufacturer;


    @ManyToOne
    @JoinColumn(name = "car_class_id")
    private CarClass carClass;

}
