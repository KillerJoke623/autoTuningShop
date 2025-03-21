package com.auto.web.services;

import com.auto.data.models.TuningOrders;
import com.auto.data.repositories.TuningOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<com. auto. data. models. Service, Integer> getRevenueByService() {
        List<TuningOrders> orders = tuningOrdersRepository.findAll();
        Map<com. auto. data. models. Service, Integer> revenueByService = new HashMap<>();

        for (TuningOrders order : orders) {
            List<com. auto. data. models. Service> services = List.copyOf(order.getServicess()); // Create a List from Set for indexed access
            List<Integer> prices = order.getPrices();

            if (services != null && prices != null && services.size() == prices.size()) {
                for (int i = 0; i < services.size(); i++) {
                    com. auto. data. models. Service service = services.get(i);
                    Integer price = prices.get(i);

                    revenueByService.merge(service, price, Integer::sum);
                }
            }
        }
        return revenueByService;
    }

    public Integer getRevenueByPeriod(LocalDate startDate, LocalDate endDate) {
        return tuningOrdersRepository.getRevenueByPeriod(startDate, endDate);
    }
}
