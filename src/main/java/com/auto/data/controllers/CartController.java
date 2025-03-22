package com.auto.data.controllers;

import com.auto.data.models.Car;
import com.auto.data.models.Service;
import com.auto.data.models.TuningOrders;
import com.auto.data.repositories.CarRepository;
import com.auto.data.repositories.ServiceRepository;
import com.auto.data.services.TuningOrdersService;
import com.auto.data.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TuningOrdersService tuningOrderService;

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        List<Service> cart = (List<Service>) session.getAttribute("cart");
        List<Integer> cartPrices = (List<Integer>) session.getAttribute("cartPrices");

        Car selectedCar = (Car) session.getAttribute("selectedCar");

        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        model.addAttribute("cartPrices", cartPrices);
        model.addAttribute("selectedCar", selectedCar);
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Integer id, @ModelAttribute("carSelect") Car userCar, HttpSession session) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));

        Car car = carRepository.findById(userCar.getCar_id()) // Получаем автомобиль из репозитория
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));

        List<Service> cart = (List<Service>) session.getAttribute("cart");
        List<Integer> cartPrices = (List<Integer>) session.getAttribute("cartPrices"); // Новый список для цен
        if (cart == null) {
            cart = new ArrayList<>();
            cartPrices = new ArrayList<>();
        }
        cart.add(service);

        // Получаем цену услуги для выбранного класса автомобиля
        int priceForCarClass = service.getService_price().get(car.getModel().getCarClass().getId() - 1); // Индексы начинаются с 0
        cartPrices.add(priceForCarClass);

        session.setAttribute("cart", cart);
        session.setAttribute("cartPrices", cartPrices); // Сохраняем цены в сессии

        // Сохраняем выбранный автомобиль в сессии
        session.setAttribute("selectedCar", car);


        return "redirect:/services"; // Перенаправление на страницу услуг
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Integer id, HttpSession session) {
        List<Service> cart = (List<Service>) session.getAttribute("cart");
        List<Integer> cartPrices = (List<Integer>) session.getAttribute("cartPrices");
        if (cart != null) {
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).getService_id().equals(id)) {
                    cart.remove(i);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        // ... (логика оформления заказа, передача данных в модель)
        List<Service> cart = (List<Service>) session.getAttribute("cart");
        List<Integer> cartPrices = (List<Integer>) session.getAttribute("cartPrices");
        Car selectedCar = (Car) session.getAttribute("selectedCar");

        TuningOrders order = new TuningOrders();

        order.setUser(userService.getCurrentUser());
        order.setDateTime(LocalDateTime.now());
        order.setCar(selectedCar);
        order.setServicess(new LinkedHashSet<>(cart));


        // Устанавливаем цены услуг в заказ
        order.setPrices(cartPrices);

        tuningOrderService.createTuningOrder(order);

        session.removeAttribute("cartServices");
        session.removeAttribute("selectedCar");
        session.removeAttribute("cartPrices");

        return "redirect:/account"; // Перенаправление на страницу оформления заказа
    }
}
