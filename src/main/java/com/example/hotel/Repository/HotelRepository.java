package com.example.hotel.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {
    
    List<Hotel> findByHotelId(String hotelId);
    Hotel findByHotelName(String hotelName);
    List<Hotel> findByCategoryCode(int categoryCode);
    List<Hotel> findByCategoryCodeAndHotelNameLike(int categoryCode, String HotelName);
    List<Hotel> findByHotelNameLike(String HotelName);
    List<Hotel> findByHotelIdOrHotelName(String hotelId, String hotelname);

    @Transactional
    void deleteByHotelId(String hotelId);
}
