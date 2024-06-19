package com.example.hotel.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.hotel.Repository.HotelRepository;
import com.example.hotel.Repository.PlanRepository;
import com.example.hotel.entity.Hotel;

@Controller
public class HotelController {
    
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    PlanRepository planRepository;

    //宿情報一覧表示用メソッド
    @RequestMapping("/adminHotel")
    public ModelAndView adminHotel(ModelAndView mv) {
        mv.addObject("planList", planRepository.findAll());
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
    public ModelAndView addH(
        @RequestParam("hotelId") String hotelId,
        @RequestParam("hotelName") String hotelName,
        @RequestParam("categoryCode") int categoryCode,
        @RequestParam("hotelAddress") String hotelAddress,
        @RequestParam("hotelCheckin") String hotelCheckin,
        @RequestParam("hotelCheckout") String hotelCheckout,
        ModelAndView mv) {

        List<Hotel> hotel = hotelRepository.findByHotelIdOrHotelName(hotelId, hotelName);

        if ( hotelId.isEmpty() || hotelName.isEmpty() || hotelAddress.isEmpty()
          || hotelCheckin.isEmpty() || hotelCheckout.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.setViewName("addHotel");
        } else if (hotel.size() != 0) {
            mv.addObject("errorMsg", "すでに登録されている宿です。");
            mv.setViewName("addHotel");
        } else {
            hotelRepository.save(new Hotel(hotelId, hotelName, categoryCode,
                                           hotelAddress, hotelCheckin, hotelCheckout));
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("errorMsg", "新規登録が完了しました。");
            mv.setViewName("adminHotel");
        }
        
        return mv;
    }


    //updateHotel.html表示用メソッド
    @RequestMapping("/updateHotel")
    public ModelAndView updateHotel(
        @RequestParam("hotelId") String hotelId,
        ModelAndView mv) {
        mv.addObject("hotel", hotelRepository.findByHotelId(hotelId).get(0));
        mv.setViewName("updateHotel");
        return mv;
    }

    //宿情報更新用メソッド
    @RequestMapping("/updateH")
    public ModelAndView updateH(
        @RequestParam("hotelId") String hotelId,
        @RequestParam("hotelName") String hotelName,
        @RequestParam("categoryCode") int categoryCode,
        @RequestParam("hotelAddress") String hotelAddress,
        @RequestParam("hotelCheckin") String hotelCheckin,
        @RequestParam("hotelCheckout") String hotelCheckout,
        ModelAndView mv) {

        List<Hotel> hotel = hotelRepository.findByHotelId(hotelId);

        if ( hotelId.isEmpty() || hotelName.isEmpty() || hotelAddress.isEmpty()
          || hotelCheckin.isEmpty() || hotelCheckout.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.addObject("hotel", hotel.get(0));
            mv.setViewName("addHotel");
        } else {
            if (!(hotel.get(0).getHotelName().equals(hotelName))) {
                Hotel hotelN = hotelRepository.findByHotelName(hotelName);
                if (hotelN != null) {
                    mv.addObject("errorMsg", "宿名が既に登録されています。");
                    mv.addObject("hotel", hotel.get(0));
                    mv.setViewName("updateHotel");
                } else {
                    hotel.get(0).setUpdateHotel(hotelId, hotelName, categoryCode, hotelAddress, hotelCheckin, hotelCheckout);
                    hotelRepository.save(hotel.get(0));
                    mv.addObject("errorMsg", "更新が完了しました。");
                    mv.addObject("hotelList", hotelRepository.findAll());
                    mv.setViewName("adminHotel");
                }
            } else {
                hotel.get(0).setUpdateHotel(hotelId, hotelName, categoryCode, hotelAddress, hotelCheckin, hotelCheckout);
                hotelRepository.save(hotel.get(0));
                mv.addObject("errorMsg", "更新が完了しました。");
                mv.addObject("hotelList", hotelRepository.findAll());
                mv.setViewName("adminHotel");
            }
        }
        
        return mv;
    }

    // 宿検索メソッド
    @RequestMapping("/findHotel")
    public ModelAndView findHotel(
        @RequestParam(name = "serchCode", defaultValue = "0") int serchCode,
        @RequestParam("freeWord") String freeWord,
        ModelAndView mv) {

        if (freeWord.equals("") && serchCode == 0){
            mv.addObject("hotelList", hotelRepository.findAll());
        } else if (serchCode == 0) {
            mv.addObject("hotelList", hotelRepository.findByHotelNameLike("%"+freeWord+"%"));
        } else if (freeWord.equals("")) {
            mv.addObject("hotelList", hotelRepository.findByCategoryCode(serchCode-1));
        } else {
            mv.addObject("hotelList", 
                         hotelRepository.findByCategoryCodeAndHotelNameLike(serchCode-1, "%"+freeWord+"%"));
        }

        mv.setViewName("adminHotel");
        return mv;
    }

    //removeHotel.html表示用メソッド
    @RequestMapping("/removeHotel")
    public ModelAndView removeHotel(
        @RequestParam("hotelId") String hotelId,
        ModelAndView mv) {
        mv.addObject("hotel", hotelRepository.findByHotelId(hotelId).get(0));
        mv.setViewName("removeHotel");
        return mv;
    }

    //宿情報削除用メソッド
    @RequestMapping("/removeH")
    public ModelAndView removeH(
        @RequestParam("hotelId") String hotelId,
        ModelAndView mv) {
        hotelRepository.deleteByHotelId(hotelId);
        mv.addObject("hotelList", hotelRepository.findAll());
        mv.setViewName("adminHotel");
        return mv;
    }
}