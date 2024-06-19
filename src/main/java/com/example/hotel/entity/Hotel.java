package com.example.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "hotel_address")
    private String hotelAddress;

    @Column(name = "hotel_checkin")
    private String hotelCheckin;

    @Column(name = "hotel_checkout")
    private String hotelCheckout;

    public Hotel() {

    }

    public Hotel(String hotelId, String hotelName, int categoryCode,
                  String hotelAddress, String hotelCheckin, String hotelCheckout) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.categoryCode = categoryCode;
        this.hotelAddress = hotelAddress;
        this.hotelCheckin = hotelCheckin;
        this.hotelCheckout = hotelCheckout;
    }

    public void setUpdateHotel(String hotelId, String hotelName, int categoryCode,
                  String hotelAddress, String hotelCheckin, String hotelCheckout) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.categoryCode = categoryCode;
        this.hotelAddress = hotelAddress;
        this.hotelCheckin = hotelCheckin;
        this.hotelCheckout = hotelCheckout;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public String getHotelCheckin() {
        return hotelCheckin;
    }

    public String getHotelCheckout() {
        return hotelCheckout;
    }
}
