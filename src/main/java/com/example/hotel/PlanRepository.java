package com.example.hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByPlanId(int planId);
    List<Plan> findByHotelId(String hotelId);
}
