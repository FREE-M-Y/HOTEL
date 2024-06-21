package com.example.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    public Review() {

    }

    public Review(int memberId, String hotelId,
                  int rating, String comment) {
        this.memberId = memberId;
        this.hotelId = hotelId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
