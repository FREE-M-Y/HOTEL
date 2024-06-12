package com.example.hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanTypeRepository extends JpaRepository<PlanType, Integer> {
    
    List<PlanType> findByPlanTypeIdOrPlanName(int planTypeId, String planName);
    PlanType findByPlanName(String planName);
}
