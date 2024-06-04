package com.auto.data.repositories;

import com.auto.data.models.TuningOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;

import java.util.List;

public interface TuningOrdersRepository extends JpaRepository<TuningOrders, Long> {
    @Query("SELECT t FROM TuningOrders t WHERE t.user.user_id = :user_id")
    List<TuningOrders> findByUser_User_id(Long user_id);

    @Query("SELECT t.status, COUNT(t) FROM TuningOrders t GROUP BY t.status")
    List<Object[]> countOrdersByStatus();

    @Query("SELECT s, SUM(p) FROM TuningOrders t JOIN t.servicess s JOIN t.prices p GROUP BY s")
    List<Object[]> getRevenueByService();

    @Query("SELECT SUM(p) FROM TuningOrders t JOIN t.prices p WHERE t.dateTime BETWEEN :startDate AND :endDate")
    Integer getRevenueByPeriod(LocalDate startDate, LocalDate endDate);

}