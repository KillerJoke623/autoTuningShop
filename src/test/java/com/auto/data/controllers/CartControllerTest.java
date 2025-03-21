package com.auto.data.controllers;

import com.auto.data.models.*;
import com.auto.data.repositories.CarRepository;
import com.auto.data.repositories.ServiceRepository;
import com.auto.data.services.TuningOrdersService;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @Autowired
    private ServiceRepository serviceRepository;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private TuningOrdersService tuningOrderService;

    @Mock
    private HttpSession session;

    @Test
    public void testAddToCart() {
        int serviceId = 1;
        int carId = 1;

        Service service = new Service();
        service.setService_id(serviceId);
        service.setService_price(Arrays.asList(1000, 1500, 2000)); // Пример цен для разных классов

        Car car = new Car();
        car.setCar_id(carId);
        Model model = new Model();
        model.setCarClass(new CarClass(1, "Class A", "", null)); // Пример класса автомобиля
        car.setModel(model);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Настройка мока для сессии (опционально)
        when(session.getAttribute("cart")).thenReturn(new ArrayList<>());
        when(session.getAttribute("cartPrices")).thenReturn(new ArrayList<>());

        String viewName = cartController.addToCart(serviceId, car, session);

        assertEquals("redirect:/services", viewName); // Проверяем редирект

//        // Проверяем, что услуга и цена добавлены в корзину в сессии (опционально)
//        verify(session).setAttribute("cart", argThat(list -> list.contains(service)));
//        verify(session).setAttribute("cartPrices", argThat(list -> list.contains(1000))); // Цена для Class A

        ArgumentCaptor<List<Service>> cartCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Integer>> cartPricesCaptor = ArgumentCaptor.forClass(List.class);

        verify(session).setAttribute("cart", cartCaptor.capture());
        verify(session).setAttribute("cartPrices", cartPricesCaptor.capture());

        List<Service> capturedCart = cartCaptor.getValue();
        List<Integer> capturedCartPrices = cartPricesCaptor.getValue();

        assertTrue(capturedCart.contains(service));
        assertTrue(capturedCartPrices.contains(1000));
    }

//    @Test
//    public void testRemoveFromCart() {
//        // Создаем тестовые данные
//        Service service1 = new Service(1, "Service 1", "Description 1", Arrays.asList(100, 200, 300), null);
//        Service service2 = new Service(2, "Service 2", "Description 2", Arrays.asList(400, 500, 600), null);
//
//        List<Service> cart = new ArrayList<>(Arrays.asList(service1, service2));
//        List<Integer> cartPrices = new ArrayList<>(Arrays.asList(100, 400)); // Примерные цены
//
//        // Настраиваем мок сессии
//        when(session.getAttribute("cart")).thenReturn(cart);
//        when(session.getAttribute("cartPrices")).thenReturn(cartPrices);
//
//        // Вызываем тестируемый метод
//        String viewName = cartController.removeFromCart(serviceIdToRemove, session);
//
//        // Проверяем результат
//        assertEquals("redirect:/cart", viewName); // Убеждаемся, что возвращается правильное имя представления
//
//        // Проверяем, что услуга и цена удалены из корзины в сессии
//        verify(session).setAttribute("cart", argThat(list -> !list.contains(serviceToRemove)));
//        verify(session).setAttribute("cartPrices", argThat(list -> !list.contains(priceToRemove)));
//    }
//
//    @Test
//    public void testCheckout() {
//        // Создаем тестовые данные (корзина, автомобиль, пользователь и т.д.)
//
//        // Настраиваем моки для session, userService, carService и tuningOrderService
//
//        String viewName = cartController.checkout(model, session);
//
//        assertEquals("redirect:/account", viewName); // Проверяем редирект
//
//        // Проверяем, что заказ создан и корзина очищена
//        verify(tuningOrderService, times(1)).createTuningOrder(any(TuningOrders.class));
//        verify(session).removeAttribute("cart");
//        verify(session).removeAttribute("selectedCar");
//        verify(session).removeAttribute("cartPrices");
//    }

}
