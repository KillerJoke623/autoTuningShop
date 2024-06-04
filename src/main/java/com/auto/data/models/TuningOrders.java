package com.auto.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TuningOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;
    @ManyToOne
    @JoinColumn(name = "users_user_id")
    private Users user;

    private String comment;
    @ManyToOne
    @JoinColumn(name = "car_car_id")
    private Car car;



    private LocalDateTime dateTime;
    private String status;



    @OneToMany(mappedBy = "tuningOrders", orphanRemoval = true)
    private List<CarPart> carParts = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "TuningOrders_services",
            joinColumns = @JoinColumn(name = "tuningOrders_order_id"),
            inverseJoinColumns = @JoinColumn(name = "services_service_id"))
    private Set<Service> servicess = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "order_prices", joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "price_index")
    private List<Integer> prices = new ArrayList<>();

}
