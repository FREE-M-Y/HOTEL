package com.example.hotel.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotel.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByPlanId(int planId);
    List<Plan> findByHotelId(String hotelId);
    List<Plan> findByPlanTypeId(int planTypeId);
}
