package com.auto.data.repositories;

import com.auto.data.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

    @Query("SELECT COUNT(DISTINCT t.user) FROM TuningOrders t")
    long countDistinctByTuningOrdersesIsNotNull();
}