package com.auto.data.controllers;

import com.auto.data.models.Manufacturers;
import com.auto.data.models.Service;
import com.auto.data.models.TuningOrders;
import com.auto.data.models.Users;
import com.auto.data.repositories.CarRepository;
import com.auto.data.repositories.ManufacturersRepository;
import com.auto.data.repositories.ModelRepository;
import com.auto.data.repositories.ServiceRepository;
import com.auto.data.services.TuningOrdersService;
import com.auto.data.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.auto.data.models.Car;


@Controller
@RequestMapping("/service/{serviceId}")
public class TuningOrdersController {
    @Autowired
    private TuningOrdersService tuningOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ManufacturersRepository manufacturersRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping
    public String createTuningOrder(@ModelAttribute("tuningOrders") TuningOrders tuningOrder, @ModelAttribute("service") Service service, @ModelAttribute("models") com.auto.data.models.Model models) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        tuningOrder.setUser(userService.getUserByEmail(userName));
        tuningOrder.setDateTime(LocalDateTime.now());

        Car car = new Car();

        car.setModel(models);
        car.setManufacturers(car.getModel().getManufacturer());
        car.setUsers(tuningOrder.getUser());
        carRepository.save(car);
        tuningOrder.setCar(car);


        Set<Service> servicesList = new LinkedHashSet<Service>();
        servicesList.add(service);
        tuningOrder.setServicess(servicesList);

        tuningOrderService.createTuningOrder(tuningOrder);



        return "newTuningOrder";
    }

    @GetMapping
    public String showTuningOrderPage(Model model, @PathVariable Integer serviceId) {
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new IllegalArgumentException("Service not found"));
        List<Manufacturers> manufacturers = manufacturersRepository.findAll();
        List<com.auto.data.models.Model> models = modelRepository.findAll();
        Users user = userService.getUserByEmail( SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("models", models);
        model.addAttribute("service", service);
        model.addAttribute("tuningOrders", new TuningOrders());
        model.addAttribute("user", user);

        return "newTuningOrder";
    }
}
