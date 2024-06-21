package com.example.hotel.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.hotel.Repository.HotelRepository;
import com.example.hotel.Repository.MemberRepository;
import com.example.hotel.Repository.PlanRepository;
import com.example.hotel.Repository.PlanTypeRepository;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Member;
import com.example.hotel.entity.Plan;
import com.example.hotel.entity.PlanType;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlanTypeRepository planTypeRepository;

    @Autowired
    HttpSession session;

    // ログイン画面（login.html）表示用メソッド
    @RequestMapping("/")
    public ModelAndView users(ModelAndView mv) {
        session.invalidate();
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView login(ModelAndView mv,
            @RequestParam("memberEmail") String memberEmail,
            @RequestParam("memberPass") String memberPass) {

        Member member = memberRepository.findByMemberEmailAndMemberPass(memberEmail, memberPass);

        if (memberEmail.equals("") || memberPass.equals("")) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.setViewName("login");
        } else if (Objects.isNull(member)) {
            mv.addObject("errorMsg", "メールアドレスかパスワードが正しくありません。");
            mv.setViewName("login");
        } else if (member.getMemberId() == 1) {
            //管理者ログイン
            mv.setViewName("admin");
            List<Member> memberList = memberRepository.findAll();
            mv.addObject("memberList", memberList);
        } else {
            if (member.getMemberWithdrawal() != null) {
                mv.addObject("errorMsg", "退会済みの会員です。");
                mv.setViewName("login");
            } else {
                session.setAttribute("memberPass", member);
                mv.addObject("member", session.getAttribute("memberPass"));
                List<Hotel> hotelList = hotelRepository.findAll();
                List<Plan> planList = planRepository.findAll();
                mv.addObject("planTypeList", planTypeRepository.findAll());
                mv.addObject("hotelList", hotelList);
                mv.addObject("planList", planList);
                mv.addObject("member", member);
                mv.setViewName("member");
            }
        }

        return mv;
    }

    // 新規登録画面（addUser.html）表示用メソッド
    @RequestMapping("/addUser")
    public ModelAndView showAdd(ModelAndView mv) {
        mv.setViewName("addUser");
        return mv;
    }

    // 新規登録メソッド
    @RequestMapping("/add")
    public ModelAndView add(
            @RequestParam("memberName") String memberName,
            @RequestParam("memberAddress") String memberAddress,
            @RequestParam("memberTel") String memberTel,
            @RequestParam("memberEmail") String memberEmail,
            @RequestParam("memberBirth") String memberBirth,
            @RequestParam("memberPass") String memberPass,
            ModelAndView mv) {

        Member member = memberRepository.findByMemberEmail(memberEmail);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = dateFormat.format(date);

        if ( memberName.isEmpty() || memberAddress.isEmpty() || memberTel.isEmpty()
          || memberEmail.isEmpty() || memberBirth.isEmpty() || memberPass.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
        } else if (member != null) {
            mv.addObject("errorMsg", "メールアドレスが既に登録されています。");
        } else {
            memberRepository.save(new Member(memberName, memberAddress, memberTel,
                                            memberEmail, memberBirth, strDate, memberPass));
            mv.addObject("member", memberRepository.findAll());
            mv.addObject("addOk", "新規登録が完了しました。");
        }
        mv.setViewName(member != null ? "addUser" : "login");
        return mv;
    }

    /***********管理者ログイン******************/
    //admin.html表示用メソッド
    @RequestMapping("admin")
    public ModelAndView admin(
        ModelAndView mv ) {
        
        mv.addObject("memberList", memberRepository.findAll());
        mv.setViewName("admin");
        return mv;
    }

    //removeMember.html表示用メソッド
    @RequestMapping("removeMember")
    public ModelAndView removeMember(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        List<Member> member = memberRepository.findByMemberId(memberId);

        if (memberId == 1) {
            mv.addObject("errorMsg", "管理用アカウントは退会できません。");
            mv.addObject("memberList", memberRepository.findAll());
            mv.setViewName("admin");
        } else if (member.get(0).getMemberWithdrawal() != null) {
            mv.addObject("errorMsg", "退会済みのアカウントです。");
            mv.addObject("memberList", memberRepository.findAll());
            mv.setViewName("admin");
        } else {
            mv.addObject("member", memberRepository.findByMemberId(memberId));
            mv.setViewName("removeMember");
        }
        return mv;
    }

    //会員削除メソッド
    @RequestMapping("removeM")
    public ModelAndView removeM(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        List<Member> member = memberRepository.findByMemberId(memberId);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = dateFormat.format(date);
        
        member.get(0).setMemberWithdrawal(strDate);
        memberRepository.save(member.get(0));

        mv.addObject("memberList", memberRepository.findAll());
        mv.setViewName("admin");
        return mv;
    }

    // 会員検索メソッド
    @RequestMapping("/findMember")
    public ModelAndView findMember(
        @RequestParam(name = "serchCode", defaultValue = "0") int serchCode,
        @RequestParam("freeword") String freeWord,
        ModelAndView mv) {

        if (freeWord.equals("")){
            List<Member> memberList = (List<Member>) memberRepository.findAll();
            mv.addObject("memberList", memberList);
        } else {
            switch (serchCode) {
            case 0:
            try {
                int memberId = Integer.parseInt(freeWord);
                List<Member> memberList = memberRepository.findByMemberId(memberId);
                mv.addObject("memberList", memberList);
            } catch (NumberFormatException e) {
                mv.addObject("errorMsg", "整数を入力してください。");
            }
                break;
            case 1:
                List<Member> memberName = memberRepository.findByMemberNameLike("%" + freeWord + "%");
                mv.addObject("memberList", memberName);
                break;
            case 2:
                List<Member> memberAddress = memberRepository.findByMemberAddressLike("%" + freeWord + "%");
                mv.addObject("memberList", memberAddress);
                break;
            default:
                List<Member> memberList2 = (List<Member>) memberRepository.findAll();
                mv.addObject("memberList", memberList2);
            }
        }

        mv.setViewName("admin");
        return mv;
    }

    //updateMember.html表示用メソッド
    @RequestMapping("updateMember")
    public ModelAndView updateMember(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        Member member = memberRepository.findByMemberId(memberId).get(0);

        if (member.getMemberWithdrawal() != null) {
            mv.addObject("errorMsg", "退会済みのアカウントです。");
            mv.addObject("memberList", memberRepository.findAll());
            mv.setViewName("admin");
        } else {
            mv.addObject("member", member);
            mv.setViewName("updateMember");
        }
        return mv;
    }

    //会員情報更新メソッド
    @RequestMapping("updateM")
    public ModelAndView updateM(
        @RequestParam("memberId") int memberId,
        @RequestParam("memberName") String memberName,
        @RequestParam("memberAddress") String memberAddress,
        @RequestParam("memberTel") String memberTel,
        @RequestParam("memberEmail") String memberEmail,
        @RequestParam("memberBirth") String memberBirth,
        @RequestParam("memberPass") String memberPass,
        ModelAndView mv ) {

        Member member = memberRepository.findByMemberId(memberId).get(0);
        
        if ( memberName.isEmpty() || memberAddress.isEmpty() || memberTel.isEmpty()
            || memberEmail.isEmpty() || memberBirth.isEmpty() || memberPass.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.setViewName("updateMember");
        } else {
            if (!(member.getMemberEmail().equals(memberEmail))) {
                Member memberEm = memberRepository.findByMemberEmail(memberEmail);
                if (memberEm != null) {
                    mv.addObject("errorMsg", "メールアドレスが既に登録されています。");
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                    mv.setViewName("updateMember");
                } else {
                    member.setUpdateMember(memberName, memberAddress, memberTel, memberEmail, memberBirth, memberPass);
                    memberRepository.save(member);

                    mv.addObject("errorMsg", "更新が完了しました。");
                    mv.addObject("memberList", memberRepository.findAll());
                    mv.setViewName("admin");
                }
            } else {
                member.setUpdateMember(memberName, memberAddress, memberTel, memberEmail, memberBirth, memberPass);
                memberRepository.save(member);

                mv.addObject("errorMsg", "更新が完了しました。");
                mv.addObject("memberList", memberRepository.findAll());
                mv.setViewName("admin");
            }
        }
        return mv;
    }

    /***********一般者ログイン******************/
    //memebr.html表示用メソッド
    @RequestMapping("member")
    public ModelAndView member(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {
        mv.addObject("planList", planRepository.findAll());
        mv.addObject("hotelList", hotelRepository.findAll());
        mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
        mv.addObject("planTypeList", planTypeRepository.findAll());
        mv.setViewName("member");
        return mv;
    }

    //editMember.html表示用メソッド
    @RequestMapping("editMember")
    public ModelAndView editMember(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        Member member = memberRepository.findByMemberId(memberId).get(0);
        mv.addObject("member", member);
        mv.setViewName("editMember");

        return mv;
    }

    //会員情報更新メソッド
    @RequestMapping("editM")
    public ModelAndView editM(
        @RequestParam("memberId") int memberId,
        @RequestParam("memberName") String memberName,
        @RequestParam("memberAddress") String memberAddress,
        @RequestParam("memberTel") String memberTel,
        @RequestParam("memberEmail") String memberEmail,
        @RequestParam("memberBirth") String memberBirth,
        @RequestParam("memberPass") String memberPass,
        ModelAndView mv ) {

        Member member = memberRepository.findByMemberId(memberId).get(0);
        
        if ( memberName.isEmpty() || memberAddress.isEmpty() || memberTel.isEmpty()
            || memberEmail.isEmpty() || memberBirth.isEmpty() || memberPass.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
            mv.setViewName("updateMember");
        } else {
            if (!(member.getMemberEmail().equals(memberEmail))) {
                Member memberEm = memberRepository.findByMemberEmail(memberEmail);
                if (memberEm != null) {
                    mv.addObject("errorMsg", "メールアドレスが既に登録されています。");
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                    mv.setViewName("updateMember");
                } else {
                    member.setUpdateMember(memberName, memberAddress, memberTel, memberEmail, memberBirth, memberPass);
                    memberRepository.save(member);

                    mv.addObject("errorMsg", "更新が完了しました。");
                    mv.addObject("planTypeList", planTypeRepository.findAll());
                    mv.addObject("hotelList", hotelRepository.findAll());
                    mv.addObject("planList", planRepository.findAll());
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                    mv.setViewName("member");
                }
            } else {
                member.setUpdateMember(memberName, memberAddress, memberTel, memberEmail, memberBirth, memberPass);
                memberRepository.save(member);

                mv.addObject("errorMsg", "更新が完了しました。");
                mv.addObject("planTypeList", planTypeRepository.findAll());
                mv.addObject("hotelList", hotelRepository.findAll());
                mv.addObject("planList", planRepository.findAll());
                mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                mv.setViewName("member");
            }
        }
        return mv;
    }

    //deleteMember.html表示用メソッド
    @RequestMapping("deleteMember")
    public ModelAndView deleteMember(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
        mv.setViewName("deleteMember");

        return mv;
    }

    //会員削除メソッド
    @RequestMapping("deleteM")
    public ModelAndView deleteM(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        List<Member> member = memberRepository.findByMemberId(memberId);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = dateFormat.format(date);
        
        member.get(0).setMemberWithdrawal(strDate);
        memberRepository.save(member.get(0));

        mv.setViewName("login");
        return mv;
    }

    // 宿泊プラン検索メソッド
    @RequestMapping("/findPlan")
    public ModelAndView findPlan(
        @RequestParam(name = "serchCode", defaultValue = "0") int serchCode,
        @RequestParam("freeword") String freeWord,
        @RequestParam("memberId") int memberId,
        ModelAndView mv) {

        if (freeWord.equals("")){
            mv.addObject("planTypeList", planTypeRepository.findAll());
            mv.addObject("hotelList", hotelRepository.findAll());
            mv.addObject("planList", planRepository.findAll());
            mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
        } else {
            switch (serchCode) {
            case 0:
                List<Hotel> hotelList = hotelRepository.findByHotelNameLike("%"+freeWord+"%");
                List<Plan> planList = new ArrayList<Plan>();
                

                if (hotelRepository.findByHotelNameLike("%"+freeWord+"%").size() != 0){
                    mv.addObject("planTypeList", planTypeRepository.findAll());
                    mv.addObject("hotelList", hotelRepository.findAll());
                    
                    for (Hotel hotel:hotelList) {
                        planList = (planRepository.findByHotelId(hotel.getHotelId()));
                    }
                    mv.addObject("planList", planList);
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                } else {
                    mv.addObject("errorMsg", "検索結果がありませんでした。");
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                }
                break;
            case 1:
                List<PlanType> planTypeList = planTypeRepository.findByPlanNameLike("%"+freeWord+"%");
                List<Plan> planListCase1 = new ArrayList<Plan>();

                if (planTypeList.size() != 0){
                    mv.addObject("planTypeList", planTypeRepository.findAll());
                    mv.addObject("hotelList", hotelRepository.findAll());

                    for (PlanType planType:planTypeList) {
                        planListCase1 = (planRepository.findByPlanTypeId(planType.getPlanTypeId()));
                    }
                    mv.addObject("planList", planListCase1);
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                } else {
                    mv.addObject("errorMsg", "検索結果がありませんでした。");
                    mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                }
                break;
            default:
                mv.addObject("planTypeList", planTypeRepository.findAll());
                mv.addObject("hotelList", hotelRepository.findAll());
                mv.addObject("planList", planRepository.findAll());
                mv.addObject("member", memberRepository.findByMemberId(memberId).get(0));
                break;
            }
        }

        mv.setViewName("member");
        return mv;
    }
}