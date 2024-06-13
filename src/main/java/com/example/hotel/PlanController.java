package com.example.hotel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlanController {
    
    @Autowired
    PlanTypeRepository planTypeRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    PlanRepository planRepository;

    //addHotelPlan.html表示用メソッド
    @RequestMapping("/addHotelPlan")
    public ModelAndView addHotelPlan(
        @RequestParam("hotelId") String hotelId,
        ModelAndView mv) {
        mv.addObject("hotelId", hotelId);
        mv.setViewName("addHotelPlan");
        return mv;
    }

    //プラン登録用メソッド
    @RequestMapping("/addHP")
    public ModelAndView addHP(
        @RequestParam("planId") int planId,
        @RequestParam("hotelId") String hotelId,
        @RequestParam("planTypeId") int planTypeId,
        @RequestParam("planPrice") int planPrice,
        @RequestParam("planRoomCount") int planRoomCount,
        @RequestParam("planDescription") String planDescription,
        ModelAndView mv) {
        
        List<Plan> plan = planRepository.findByPlanId(planId);
        List<PlanType> planType = planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId,null);

        if (plan.size() != 0) {
            mv.addObject("errorMsg", "すでに登録されているプランIDです");
            mv.addObject("hotelId", hotelId);
            mv.setViewName("addHotelPlan");
        } else if (planType.size() == 0) {
            mv.addObject("errorMsg", "存在しないプランタイプです");
            mv.addObject("hotelId", hotelId);
            mv.setViewName("addHotelPlan");
        } else {
            planRepository.save(new Plan(planId, hotelId, planTypeId, planPrice, planRoomCount,
                                null, planDescription));
            
            mv.addObject("errorMsg", "プランを登録しました。");
            mv.addObject("planList",planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.setViewName("adminHotel");
        }
        return mv;
    }

    //editHotelPlan.html表示用メソッド
    @RequestMapping("/editHotelPlan")
    public ModelAndView editHotelPlan(
        @RequestParam("hotelId") String hotelId,
        ModelAndView mv) {
        mv.addObject("planList",planRepository.findByHotelId(hotelId));
        mv.setViewName("editHotelPlan");
        return mv;
    }

    //updateHotelPlan.html表示用メソッド
    @RequestMapping("/updateHotelPlan")
    public ModelAndView updateHotelPlan(
        @RequestParam("planId") int planId,
        ModelAndView mv) {
        mv.addObject("plan",planRepository.findByPlanId(planId).get(0));
        mv.setViewName("updateHotelPlan");
        return mv;
    }

    //宿プラン変更用メソッド
    @RequestMapping("/updateHP")
    public ModelAndView updateHP(
        @RequestParam("planId") int planId,
        @RequestParam("hotelId") String hotelId,
        @RequestParam("planTypeId") int planTypeId,
        @RequestParam("planPrice") int planPrice,
        @RequestParam("planRoomCount") int planRoomCount,
        @RequestParam("planDeleteDate") String planDeleteDate,
        @RequestParam("planDescription") String planDescription,
        ModelAndView mv) {
        
        List<Plan> planList = planRepository.findByPlanId(planId);
        List<PlanType> planType = planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId,null);

        if (planType.size() == 0) {
            mv.addObject("errorMsg", "存在しないプランタイプです");
            mv.addObject("plan",planRepository.findByPlanId(planId).get(0));
            mv.setViewName("updateHotelPlan");
        } else {
            planList.get(0).setEditPlan(planTypeId, planPrice, planRoomCount, planDeleteDate, planDescription);
            planRepository.save(planList.get(0));
            mv.addObject("errorMsg", "プランを変更しました。");
            mv.addObject("planList",planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.setViewName("adminHotel");
        }

        return mv;
    }

    //removeHotelPlan.html表示用メソッド
    @RequestMapping("/removeHotelPlan")
    public ModelAndView removeHotelPlan(
        @RequestParam("planId") int planId,
        ModelAndView mv) {
        mv.addObject("plan",planRepository.findByPlanId(planId).get(0));
        mv.setViewName("removeHotelPlan");
        return mv;
    }

    //宿泊プラン削除用メソッド
    @RequestMapping("/removeHP")
    public ModelAndView removeHP(
        @RequestParam("planId") int planId,
        @RequestParam("planDescription") String planDescription,
        ModelAndView mv) {

        Plan plan = planRepository.findByPlanId(planId).get(0);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = dateFormat.format(date);
        
        plan.setPlanDeleteDate(strDate, planDescription);
        planRepository.save(plan);            
        mv.addObject("errorMsg", "プランを削除しました。");
        mv.addObject("planList",planRepository.findAll());
        mv.addObject("hotelList", hotelRepository.findAll());
        mv.setViewName("adminHotel");
        return mv;
    }
}
