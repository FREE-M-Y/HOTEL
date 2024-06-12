package com.example.hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_types")
public class PlanType {
    @Id
    @Column(name = "plan_type_id")
    private int planTypeId;

    @Column(name = "plan_name")
    private String planName;

    public PlanType() {

    }

    public PlanType(int planTypeId, String planName) {
        this.planTypeId = planTypeId;
        this.planName = planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getPlanTypeId() {
        return planTypeId;
    }

    public String getPlanName() {
        return planName;
    }

}
