package com.example.hotel;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HotelController {
    
    @Autowired
    HotelRepository hotelRepository;

    @RequestMapping("/adminHotel")
    public ModelAndView adminHotel(ModelAndView mv) {
        mv.addObject("hotelList", hotelRepository.findAll());
        mv.setViewName("adminHotel");
        return mv;
    }

    //addHotel.html表示用メソッド
    @RequestMapping("/addHotel")
    public ModelAndView addHotel(ModelAndView mv) {
        mv.setViewName("addHotel");
        return mv;
    }

    //宿新規登録表示用メソッド
    @RequestMapping("/addH")
    public ModelAndView addH(ModelAndView mv) {
        mv.setViewName("addHotel");
        return mv;
    }
}
