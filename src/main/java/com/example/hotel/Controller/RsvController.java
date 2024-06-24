package com.example.hotel.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.hotel.Repository.HotelRepository;
import com.example.hotel.Repository.MemberRepository;
import com.example.hotel.Repository.PlanRepository;
import com.example.hotel.Repository.PlanTypeRepository;
import com.example.hotel.Repository.RsvRepository;
import com.example.hotel.entity.Member;
import com.example.hotel.entity.Plan;
import com.example.hotel.entity.Rsv;

@Controller
public class RsvController {

    @Autowired
    RsvRepository rsvRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    PlanTypeRepository planTypeRepository;

    //reservations.html表示用メソッド
    @RequestMapping("reservations")
    public ModelAndView reservations(
        @RequestParam("memberId") int memberId,
        @RequestParam("planId") int planId,
        ModelAndView mv ) {

        mv.addObject("plan", planRepository.findByPlanId(planId).get(0));
        mv.addObject("hotelList", hotelRepository.findAll());
        mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
        mv.addObject("planTypeList", planTypeRepository.findAll());
        mv.setViewName("reservations");

        return mv;
    }

    //宿予約メソッド
    @RequestMapping("/addRsv")
    public ModelAndView addRsv(
            @RequestParam("rsvCheckin") String rsvCheckin,
            @RequestParam("rsvCheckout") String rsvCheckout,
            @RequestParam("rsvRoomCount") int rsvRoomCount,
            @RequestParam("planId") int planId,
            @RequestParam("memberId") int memberId,
            ModelAndView mv) {

        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = dateFormat.format(date);

        Plan plan = planRepository.findByPlanId(planId).get(0);

        if ( rsvCheckin.isEmpty() || rsvCheckout.isEmpty() || rsvRoomCount == 0) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.addObject("plan", planRepository.findByPlanId(planId).get(0));
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.setViewName("reservations");
        } else if (plan.getPlanRoomCount() < rsvRoomCount) {
            mv.addObject("errorMsg", "予約可能部屋数を超えています。");
            mv.addObject("plan", planRepository.findByPlanId(planId).get(0));
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.setViewName("reservations");
        } else {
            int tempPlan = plan.getPlanRoomCount();

            tempPlan = tempPlan - rsvRoomCount;

            plan.setEditPlan(plan.getPlanTypeId(), plan.getPlanPrice(), tempPlan,
                             plan.getPlanDeleteDate(), plan.getPlanDescription());
            planRepository.save(plan);
            rsvRepository.save(new Rsv(memberId, planId, strDate, rsvCheckin, rsvCheckout,
                                       rsvRoomCount));

            List<Rsv> rsvList = rsvRepository.findByMemberId(memberId);
            Member member = memberRepository.findByMemberId(memberId).get(0);
            List<Plan> planList = planRepository.findAll();

            calcMemberPrice(member, rsvList, planList);
            memberRepository.save(member);
            
            mv.addObject("errorMsg", "予約が完了しました。");
            mv.addObject("planList", planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.setViewName("member");
        }
        return mv;
    }

    //editreservations.html表示用メソッド
    @RequestMapping("editReservations")
    public ModelAndView editReservations(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {
        List<Rsv> rsvList = rsvRepository.findByMemberId(memberId);

        if (rsvList.size() == 0) {
            mv.addObject("errorMsg", "予約した宿はありません。");
            mv.addObject("planList", planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.addObject("planTypeList", planTypeRepository.findAll());

            Member member = memberRepository.findByMemberId(memberId).get(0);
            List<Plan> planList = planRepository.findAll();
    
            calcMemberPrice(member, rsvList, planList);
            memberRepository.save(member);

            mv.setViewName("member");
        } else {
            mv.addObject("memberId", memberId);
            mv.addObject("rsvList", rsvList);
            mv.setViewName("editReservations");
        }

        return mv;
    }

    //updateRsv.html表示用メソッド
    @RequestMapping("updateRsv")
    public ModelAndView updateRsv(
        @RequestParam("rsvId") int rsvId,
        ModelAndView mv ) {

        mv.addObject("rsv", rsvRepository.findByRsvId(rsvId));
        mv.setViewName("updateRsv");

        return mv;
    }

    //予約変更用メソッド
    @RequestMapping("updateR")
    public ModelAndView updateR(
        @RequestParam("rsvCheckin") String rsvCheckin,
        @RequestParam("rsvCheckout") String rsvCheckout,
        @RequestParam("rsvRoomCount") int rsvRoomCount,
        @RequestParam("rsvId") int rsvId,
        ModelAndView mv ) {

        Rsv rsv = rsvRepository.findByRsvId(rsvId);
        Plan plan = planRepository.findByPlanId(rsv.getPlanId()).get(0);

        if (rsv.getRsvRoomCount() == rsvRoomCount) {
            rsv.setRsv(rsvCheckin, rsvCheckout, rsvRoomCount);
            rsvRepository.save(rsv);
            mv.addObject("errorMsg", "予約を変更しました。");
            mv.addObject("memberId", rsv.getMemberId());
            mv.addObject("rsvList", rsvRepository.findAll());
            mv.setViewName("editReservations");
        } else {
            if (rsv.getRsvRoomCount() < rsvRoomCount){
                if (plan.getPlanRoomCount() < rsvRoomCount) {
                    mv.addObject("errorMsg", "予約可能部屋数を超えています。");
                    mv.addObject("rsv", rsvRepository.findByRsvId(rsvId));
                    mv.setViewName("updateRsv");
                } else {
                    int tempPlan = plan.getPlanRoomCount();
                    tempPlan = tempPlan - rsvRoomCount;
                    plan.setEditPlan(plan.getPlanId(), plan.getPlanPrice(), tempPlan,
                                     plan.getPlanDeleteDate(), plan.getPlanDescription());
                    
                    rsv.setRsv(rsvCheckin, rsvCheckout, rsvRoomCount);
                    rsvRepository.save(rsv);
                    mv.addObject("errorMsg", "予約を変更しました。");
                    mv.addObject("memberId", rsv.getMemberId());
                    mv.addObject("rsvList", rsvRepository.findAll());
                    mv.setViewName("editReservations");
                }
            } else {
                if (plan.getPlanRoomCount() < rsvRoomCount) {
                    mv.addObject("errorMsg", "予約可能部屋数を超えています。");
                    mv.addObject("rsv", rsvRepository.findByRsvId(rsvId));
                    mv.setViewName("updateRsv");
                } else {
                    int tempRsv = rsv.getRsvRoomCount();
                    int tempPlan = plan.getPlanRoomCount();
                    tempRsv = tempRsv - rsvRoomCount;
                    tempPlan = tempPlan + tempRsv;
                    plan.setEditPlan(plan.getPlanId(), plan.getPlanPrice(), tempPlan,
                                     plan.getPlanDeleteDate(), plan.getPlanDescription());
                    planRepository.save(plan);
                    
                    rsv.setRsv(rsvCheckin, rsvCheckout, rsvRoomCount);
                    rsvRepository.save(rsv);
                    mv.addObject("errorMsg", "予約を変更しました。");
                    mv.addObject("memberId", rsv.getMemberId());
                    mv.addObject("rsvList", rsvRepository.findAll());
                    mv.setViewName("editReservations");
                }
            }
        }

        return mv;
    }

    //cxlRsv.html表示用メソッド
    @RequestMapping("cxlRsv")
    public ModelAndView cxlRsv(
        @RequestParam("rsvId") int rsvId,
        ModelAndView mv ) {

        mv.addObject("rsv", rsvRepository.findByRsvId(rsvId));
        mv.setViewName("cxlRsv");

        return mv;
    }

    //予約キャンセル用メソッド
    @RequestMapping("cxlR")
    public ModelAndView cxlR(
        @RequestParam("rsvId") int rsvId,
        ModelAndView mv ) {

        Rsv rsv = rsvRepository.findByRsvId(rsvId);
        Plan plan = planRepository.findByPlanId(rsv.getPlanId()).get(0);

        int tempRsv = rsv.getRsvRoomCount();
        int tempPlan = plan.getPlanRoomCount();
        tempPlan = tempPlan + tempRsv;
        plan.setEditPlan(plan.getPlanId(), plan.getPlanPrice(), tempPlan,
                         plan.getPlanDeleteDate(), plan.getPlanDescription());
        planRepository.save(plan);
        rsvRepository.deleteByRsvId(rsvId);

        mv.addObject("errorMsg", "予約をキャンセルしました。");
        mv.addObject("memberId", rsv.getMemberId());
        mv.addObject("rsvList", rsvRepository.findAll());
        mv.setViewName("editReservations");

        return mv;
    }

    //予約履歴用メソッド
    @RequestMapping("historyRsv")
    public ModelAndView historyRsv(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {
        
        List<Rsv> rsvList = rsvRepository.findByMemberId(memberId);
        List<Rsv> tempRsvlist = new ArrayList<Rsv>();
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        
        for (Rsv rsv:rsvList){
            if (rsv.getRsvCheckout().compareTo(strDate) < 0) {
                tempRsvlist.add(rsv);
            } else {
            }
        }

        if(tempRsvlist.size() != 0){
            mv.addObject("memberId", memberId);
            mv.addObject("planList", planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("rsvList", tempRsvlist);
            mv.setViewName("historyReservations");
        } else {
            mv.addObject("errorMsg", "宿泊した宿はありません。");
            mv.addObject("planList", planRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.addObject("planTypeList", planTypeRepository.findAll());

            Member member = memberRepository.findByMemberId(memberId).get(0);
            List<Plan> planList = planRepository.findAll();
    
            calcMemberPrice(member, rsvList, planList);
            memberRepository.save(member);

            mv.setViewName("member");
        }
        return mv;
    }

    public static void calcMemberPrice(Member member, List<Rsv> rsvList, List<Plan> planList){
        
        List<Rsv> tempRsvlist = new ArrayList<Rsv>();
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        double discount;
        
        for (Rsv rsv:rsvList){
            if (rsv.getRsvCheckout().compareTo(strDate) < 0) {
                tempRsvlist.add(rsv);
            } else {
            }
        }

        if (tempRsvlist.size() <= 9) {
            member.setMemberRank("Bronze");
            discount = 1.0;
        } else if (tempRsvlist.size() <= 19) {
            member.setMemberRank("Silver");
            discount = 0.95;
        } else if (tempRsvlist.size() <= 29) {
            member.setMemberRank("Gold");
            discount = 0.90;
        } else {
            member.setMemberRank("Platinum");
            discount = 0.85;
        }

        for (Plan plan:planList) {
            plan.setPlanMemberPrice((int)((double)plan.getPlanPrice() * discount));
        }
    }
}
