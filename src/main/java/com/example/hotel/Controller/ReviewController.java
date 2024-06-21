package com.example.hotel.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.hotel.Repository.HotelRepository;
import com.example.hotel.Repository.ReviewRepository;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Review;

@Controller
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    HotelRepository hotelRepository;

    //口コミ表示用メソッド
    @RequestMapping("review")
    public ModelAndView review(
    @RequestParam("hotelId") String hotelId,
    @RequestParam("memberId") int memberId,
    ModelAndView mv ) {
        
        List<Hotel> hotel = hotelRepository.findByHotelId(hotelId);
        List<Review> reviewList = reviewRepository.findByHotelId(hotelId);

        mv.addObject("hotel", hotel.get(0));
        mv.addObject("reviewList", reviewList);
        mv.addObject("memberId", memberId);
        mv.setViewName("review");
        return mv;
    }

    //口コミ投稿用メソッド
    @RequestMapping("addReview")
    public ModelAndView addReview(
    @RequestParam("hotelId") String hotelId,
    @RequestParam("memberId") int memberId,
    @RequestParam("rating") int rating,
    @RequestParam("comment") String comment,
    ModelAndView mv ) {

        reviewRepository.save(new Review(memberId, hotelId, rating, comment));

        List<Hotel> hotel = hotelRepository.findByHotelId(hotelId);
        List<Review> reviewList = reviewRepository.findByHotelId(hotelId);

        mv.addObject("hotel", hotel.get(0));
        mv.addObject("reviewList", reviewList);
        mv.addObject("memberId", memberId);
        mv.setViewName("review");
        return mv;
    }
}
