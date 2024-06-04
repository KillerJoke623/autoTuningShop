package com.auto.web.services;

import com.auto.data.models.TuningOrders;
import com.auto.data.repositories.TuningOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private TuningOrdersRepository tuningOrdersRepository;

    @Autowired
    public OrderService(TuningOrdersRepository tuningOrdersRepository) {
        this.tuningOrdersRepository = tuningOrdersRepository;
    }

    public Long getTotalOrdersCount() {
        return tuningOrdersRepository.count();
    }

    public List<Object[]> getOrdersCountByStatus() {
        return tuningOrdersRepository.countOrdersByStatus();
    }

    public int getTotalRevenue() {
        List<TuningOrders> orders = tuningOrdersRepository.findAll();
        int totalRevenue = 0;
        for (TuningOrders order : orders) {
            totalRevenue += order.getPrices().stream().mapToInt(Integer::intValue).sum();
        }
        return totalRevenue;
    }

    public List<Object[]> getRevenueByService() {
        return tuningOrdersRepository.getRevenueByService();
    }

    public Integer getRevenueByPeriod(LocalDate startDate, LocalDate endDate) {
        return tuningOrdersRepository.getRevenueByPeriod(startDate, endDate);
    }
}
