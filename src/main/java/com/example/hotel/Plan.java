package com.example.hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @Column(name = "plan_id")
    private int planId;

    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "plan_type_id")
    private int planTypeId;

    @Column(name = "plan_price")
    private int planPrice;

    @Column(name = "plan_room_count")
    private int planRoomCount;

    @Column(name = "plan_dalete_date")
    private String planDeleteDate;

    @Column(name = "plan_description")
    private String planDescription;

    public Plan() {

    }

    public Plan(int planId, String hotelId, int planTypeId, int planPrice,
                  int planRoomCount, String planDeleteDate, String planDescription) {
        this.planId = planId;
        this.hotelId = hotelId;
        this.planTypeId = planTypeId;
        this.planPrice = planPrice;
        this.planRoomCount = planRoomCount;
        this.planDeleteDate = planDeleteDate;
        this.planDescription = planDescription;
    }

    public void setEditPlan(int planTypeId, int planPrice, int planRoomCount,
                            String planDeleteDate, String planDescription) {
        this.planTypeId = planTypeId;
        this.planPrice = planPrice;
        this.planRoomCount = planRoomCount;
        this.planDeleteDate = planDeleteDate;
        this.planDescription = planDescription;
    }

    public void setPlanDeleteDate(String planDeleteDate, String planDescription) {
        this.planDeleteDate = planDeleteDate;
        this.planDescription = planDescription;
    }

    public int getPlanId() {
        return planId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public int getPlanTypeId() {
        return planTypeId;
    }

    public int getPlanPrice() {
        return planPrice;
    }

    public int getPlanRoomCount() {
        return planRoomCount;
    }

    public String getPlanDeleteDate() {
        return planDeleteDate;
    }

    public String getPlanDescription() {
        return planDescription;
    }
}
