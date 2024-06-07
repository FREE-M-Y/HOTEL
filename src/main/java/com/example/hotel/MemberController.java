package com.example.hotel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
    @Autowired
    MemberRepository memberRepository;

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
        } else if (memberEmail.equals("admin@adimin") && memberPass.equals("0")) {
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
                //List<Hotel> hotel = hotelRepository.findAll();
                //mv.addObject("hotelList", hotel);
                mv.setViewName("lodging");
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
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
    //removeMember.html表示用メソッド
    @RequestMapping("removeMember")
    public ModelAndView removeMember(
        @RequestParam("memberId") int memberId,
        ModelAndView mv ) {

        if (memberId == 1) {
            mv.addObject("errorMsg", "管理用アカウントは退会できません。");
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

        Member member = memberRepository.findByMemberId(memberId);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = dateFormat.format(date);
        
        member.setMemberWithdrawal(strDate);
        memberRepository.save(member);

        mv.addObject("member", memberRepository.findAll());
        mv.setViewName("admin");
        return mv;
    }
    
}