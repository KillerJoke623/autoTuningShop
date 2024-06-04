package com.auto.data.controllers;

import com.auto.data.models.CarClass;
import com.auto.data.models.Manufacturers;
import com.auto.data.models.TuningOrders;
import com.auto.data.repositories.*;
import com.auto.data.services.CarService;
import com.auto.data.services.UserService;
import com.auto.web.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.auto.data.models.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private TuningOrdersRepository tuningOrdersRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ManufacturersRepository manufacturersRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private CarClassRepository carClassRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;


    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<TuningOrders> orders = tuningOrdersRepository.findAll(); // Получаем все заказы
        model.addAttribute("orders", orders); // Передаем заказы в модель
        return "admin/orders"; // Возвращаем имя шаблона для отображения
    }

    @PostMapping("/orders/{id}/updateStatus")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam("status") String newStatus) {
        TuningOrders order = tuningOrdersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        // Проверяем допустимость нового статуса (на всякий случай)
        List<String> allowedStatuses = Arrays.asList("pending", "in_work", "done", "declined");
        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Недопустимый статус: " + newStatus);
        }
        order.setStatus(newStatus);
        tuningOrdersRepository.save(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/services")
    public String showServices(Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "admin/services"; // Имя шаблона для отображения списка услуг
    }

    @GetMapping("/services/add")
    public String addServiceForm(Model model) {
        model.addAttribute("service", new Service());
        return "admin/addService"; // Имя шаблона для формы добавления услуги
    }

    @PostMapping("/services/add")
    public String addService(@ModelAttribute("service") Service service, @RequestParam("imageFile") MultipartFile imageFile) {
        serviceRepository.save(service);

        if (!imageFile.isEmpty()) {
            try {
                String fileName = service.getService_id() + ".jpg"; // Используем id услуги в качестве имени файла
                Path filePath = Paths.get("src/main/resources/static/images", fileName); // Путь к папке с изображениями
                Files.createFile(filePath);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Обработка ошибки загрузки файла
                e.printStackTrace();
            }
        }
        return "redirect:/admin/services";
    }

    @GetMapping("/services/edit/{id}")
    public String editServiceForm(@PathVariable Integer id, Model model) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
        model.addAttribute("service", service);
        return "admin/editService"; // Имя шаблона для формы редактирования услуги
    }

    @PostMapping("/services/edit/{id}")
    public String editService(@PathVariable Integer id, @ModelAttribute("service") Service updatedService ,
                              @RequestParam("imageFile") MultipartFile imageFile) {

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
        service.setService_name(updatedService.getService_name());
        service.setService_description(updatedService.getService_description());
        service.setService_price(updatedService.getService_price());
        serviceRepository.save(service);

        if (!imageFile.isEmpty()) {
            try {
                String fileName = updatedService.getService_id() + ".jpg";
                Path filePath = Paths.get("src/main/resources/static/images", fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/admin/services";
    }

    @PostMapping("/services/delete/{id}")
    public String deleteService(@PathVariable Integer id) {
        serviceRepository.deleteById(id);
        return "redirect:/admin/services";
    }

    // Производители
    @GetMapping("/manufacturers")
    public String showManufacturers(Model model) {
        List<Manufacturers> manufacturers = manufacturersRepository.findAll();
        model.addAttribute("manufacturers", manufacturers);
        return "admin/manufacturers"; // Шаблон для списка производителей
    }

    @GetMapping("/manufacturers/add")
    public String addManufacturerForm(Model model) {
        model.addAttribute("manufacturers", new Manufacturers());
        return "admin/addManufacturer"; // Шаблон для формы добавления производителя
    }

    @PostMapping("/manufacturers/add")
    public String addManufacturer(@ModelAttribute("manufacturers") Manufacturers manufacturer) {
        manufacturersRepository.save(manufacturer);
        return "redirect:/admin/manufacturers";
    }

    // Модели
    @GetMapping("/models")
    public String showModels(Model model) {
        List<com.auto.data.models.Model> models = modelRepository.findAll();
        model.addAttribute("models", models);
        return "admin/models"; // Шаблон для списка моделей
    }

    @GetMapping("/models/add")
    public String addModelForm(Model model) {
        model.addAttribute("model", new com.auto.data.models.Model());
        model.addAttribute("manufacturers", manufacturersRepository.findAll()); // Список производителей для выбора
        model.addAttribute("carClasses", carClassRepository.findAll());
        return "admin/addModel"; // Шаблон для формы добавления модели
    }

    @PostMapping("/models/add")
    public String addModel(@ModelAttribute("model") com.auto.data.models.Model model) {
        modelRepository.save(model);
        return "redirect:/admin/models";
    }

    @GetMapping("/models/edit/{id}")
    public String editModelForm(@PathVariable Long id, Model model) {
        com.auto.data.models.Model carModel = modelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Модель не найдена"));;
        model.addAttribute("model", carModel);
        model.addAttribute("carClasses", carClassRepository.findAll()); // Добавляем список классов автомобилей
        return "admin/editModel";
    }

    @PostMapping("/models/edit/{id}")
    public String editModel(@PathVariable Long id, @ModelAttribute("model") com.auto.data.models.Model updatedModel) {
        com.auto.data.models.Model model = modelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Модель не найдена"));

        model.setModel_name(updatedModel.getModel_name());
        model.setGeneration(updatedModel.getGeneration());

        // Получаем объект CarClass по ID
        CarClass carClass = carClassRepository.findById(updatedModel.getCarClass().getId()).orElse(null);
        model.setCarClass(carClass);

//        // Получаем объект Manufacturers по ID
//        Manufacturers manufacturer = manufacturersRepository.findById(updatedModel.getManufacturer().getManufacturer_id()).orElse(null);
//        model.setManufacturer(manufacturer);

        modelRepository.save(model);
        return "redirect:/admin/models";
    }

    @GetMapping("/analytics")
    public String showAnalytics(Model model) {
        model.addAttribute("totalOrdersCount", orderService.getTotalOrdersCount());
        model.addAttribute("ordersCountByStatus", orderService.getOrdersCountByStatus());
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("revenueByService", orderService.getRevenueByService());
        model.addAttribute("totalUsersCount", userService.getTotalUsersCount());
        model.addAttribute("activeUsersCount", userService.getActiveUsersCount());
        model.addAttribute("mostPopularBrands", carService.getMostPopularBrands());
        model.addAttribute("mostPopularModels", carService.getMostPopularModels());
        model.addAttribute("carsDistributionByClass", carService.getCarsDistributionByClass());
        // Добавьте другие метрики по мере необходимости
        return "admin/analytics";
    }

}
