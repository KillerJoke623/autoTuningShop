package com.auto.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer service_id;

    private String service_name;
    private String service_description;
    @ElementCollection
    @CollectionTable(name = "service_prices",
            joinColumns = @JoinColumn(name = "service_id"))
    @OrderColumn(name = "price_index")
    private List<Integer> service_price = new ArrayList<>();




    @ManyToMany(mappedBy = "servicess")
    private Set<TuningOrders> tuningOrderses = new LinkedHashSet<>();

}
