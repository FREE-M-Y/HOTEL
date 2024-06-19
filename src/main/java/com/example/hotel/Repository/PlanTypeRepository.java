package com.example.hotel.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotel.entity.PlanType;

@Repository
public interface PlanTypeRepository extends JpaRepository<PlanType, Integer> {
    
    List<PlanType> findByPlanTypeIdOrPlanName(int planTypeId, String planName);
    PlanType findByPlanName(String planName);
}
