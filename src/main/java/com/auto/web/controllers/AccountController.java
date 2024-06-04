package com.auto.web.controllers;

import com.auto.data.models.Car;
import com.auto.data.models.Manufacturers;
import com.auto.data.models.TuningOrders;
import com.auto.data.models.Users;
import com.auto.data.repositories.CarRepository;
import com.auto.data.repositories.ManufacturersRepository;
import com.auto.data.repositories.ModelRepository;
import com.auto.data.repositories.TuningOrdersRepository;

import com.auto.data.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private TuningOrdersRepository tuningOrdersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ManufacturersRepository manufacturersRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/account")
    public String getAccount(Model model) {
        Users user = userService.getUserByEmail( SecurityContextHolder.getContext().getAuthentication().getName());
        //!TODO Manufacturer Model to make add car button work
        List<TuningOrders> orders = tuningOrdersRepository.findByUser_User_id(user.getUser_id());

        List<Manufacturers> manufacturers = manufacturersRepository.findAll();
        List<com.auto.data.models.Model> models = modelRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("models", models);

        return "account";
    }

    @PostMapping("/account/createCar")
    public String createCar(@ModelAttribute("car") Car car, @ModelAttribute("models") com.auto.data.models.Model models) {
        // Получаем текущего пользователя (предполагается, что вы храните его в сессии после аутентификации)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.getUserByEmail( SecurityContextHolder.getContext().getAuthentication().getName());

        car.setModel(models);
        // Устанавливаем владельца автомобиля
        car.setUsers(user);

        // Сохраняем автомобиль в базе данных
        carRepository.save(car);

        return "redirect:/account"; // Перенаправляем на страницу аккаунта
    }


}
