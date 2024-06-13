package com.example.hotel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlanTypeController {

    @Autowired
    PlanTypeRepository planTypeRepository;

    @Autowired
    PlanRepository planRepository;

    //プラン情報一覧表示用メソッド
    @RequestMapping("adminPlanType")
    public ModelAndView adminPlanType(
        ModelAndView mv ) {
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.addObject("planList", planRepository.findAll());
            mv.setViewName("adminPlanType");
            return mv;
    }

    //addPlanType.html表示用メソッド
    @RequestMapping("addPlanType")
    public ModelAndView addPlanType(
        ModelAndView mv ) {
            mv.setViewName("addPlanType");
            return mv;
    }

    //プランタイプ新規登録用メソッド
    @RequestMapping("addPT")
    public ModelAndView addPT(
        @RequestParam(name = "planTypeId", defaultValue="0") int planTypeId,
        @RequestParam("planName") String planName,
        ModelAndView mv ) {
            
        List<PlanType> planType = planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId, planName);
        if (planName.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.setViewName("addPlanType");
        } else if (planType.size() != 0) {
            mv.addObject("errorMsg", "すでに登録されているプランタイプです。");
            mv.setViewName("addPlanType");
        } else {
            planTypeRepository.save(new PlanType(planTypeId,planName));
            mv.addObject("errorMsg", "プランタイプを登録しました。");
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.setViewName("adminPlanType");
        }
        return mv;
    }

    //updatePlnaType.html表示用メソッド
    @RequestMapping("updatePlanType")
    public ModelAndView updatePlnaType(
        @RequestParam("planTypeId") int planTypeId,
        ModelAndView mv ) {
        mv.addObject("planType", planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId,null).get(0));
        mv.setViewName("updatePlanType");
       return mv;
    }

    //プランタイプ更新用メソッド
    @RequestMapping("updatePT")
    public ModelAndView updatePT(
        @RequestParam("planTypeId") int planTypeId,
        @RequestParam("planName") String planName,
        ModelAndView mv ) {
        
        PlanType planType = planTypeRepository.findByPlanName(planName);

        if (planType != null) {
            mv.addObject("errorMsg", "すでに使用されているプラン名です。");
            mv.addObject("planType", planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId,null).get(0));
            mv.setViewName("updatePlanType");
        } else {
            PlanType tempPlanType = planTypeRepository.findByPlanTypeIdOrPlanName(planTypeId,null).get(0);
            tempPlanType.setPlanName(planName);
            planTypeRepository.save(tempPlanType);
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.setViewName("adminPlanType");
        }
       return mv;
    }
}
