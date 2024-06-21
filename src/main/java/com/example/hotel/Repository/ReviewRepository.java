package com.example.hotel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotel.entity.Review;
import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByHotelId(String hotelId);
}
