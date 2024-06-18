package com.example.hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Rsv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rsv_id")
    private int rsvId;

    @Column(name = "member_id ")
    private int memberId;

    @Column(name = "plan_id ")
    private int planId;

    @Column(name = "rsv_date")
    private String rsvDate;

    @Column(name = "rsv_checkin")
    private String rsvCheckin;

    @Column(name = "rsv_checkout")
    private String rsvCheckout;

    @Column(name = "rsv_room_count")
    private int rsvRoomCount;

    public Rsv() {

    }

    public Rsv(int memberId, int planId,
                  String rsvDate, String rsvCheckin, String rsvCheckout, int rsvRoomCount) {
        this.memberId = memberId;
        this.planId = planId;
        this.rsvDate = rsvDate;
        this.rsvCheckin = rsvCheckin;
        this.rsvCheckout = rsvCheckout;
        this.rsvRoomCount = rsvRoomCount;
    }

    public void setRsv(String rsvCheckin, String rsvCheckout, int rsvRoomCount) {
        this.rsvCheckin = rsvCheckin;
        this.rsvCheckout = rsvCheckout;
        this.rsvRoomCount = rsvRoomCount;
    }

    public int getRsvId() {
        return rsvId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getPlanId() {
        return planId;
    }

    public String getRsvDate() {
        return rsvDate;
    }

    public String getRsvCheckin() {
        return rsvCheckin;
    }

    public String getRsvCheckout() {
        return rsvCheckout;
    }

    public int getRsvRoomCount() {
        return rsvRoomCount;
    }
}
