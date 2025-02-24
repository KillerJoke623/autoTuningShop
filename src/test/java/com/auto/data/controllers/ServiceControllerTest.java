package com.auto.data.controllers;

import com.auto.data.models.Service;
import com.auto.data.repositories.ServiceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ServiceControllerTest {


    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceController serviceController;

    @Mock
    private Model model;

    @Test
    public void testGetServices() {
        // Создаем тестовые данные
        List<Service> services = Arrays.asList(
                new Service(1, "Service 1", "Description 1", Arrays.asList(100, 200, 300), null),
                new Service(2, "Service 2", "Description 2", Arrays.asList(400, 500, 600), null)
        );

        // Настраиваем мок репозитория
        when(serviceRepository.findAll()).thenReturn(services);

        // Вызываем тестируемый метод
        String viewName = serviceController.getServices(model);

        // Проверяем результат
        assertEquals("servicesPage", viewName); // Убеждаемся, что возвращается правильное имя представления
        verify(model).addAttribute("services", services); // Убеждаемся, что данные переданы в модель
    }
}
