package com.example.hotel;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        } else {
            session.setAttribute("memberPass", member);
            mv.addObject("member", session.getAttribute("memberPass"));
            //List<Hotel> hotel = hotelRepository.findAll();
            //mv.addObject("hotelList", hotel);
            mv.setViewName("lodging");
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

        Member memberE = memberRepository.findByMemberEmail(memberEmail);
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        if ( memberName.isEmpty() || memberAddress.isEmpty() || memberTel.isEmpty()
          || memberEmail.isEmpty() || memberBirth.isEmpty() || memberPass.isEmpty()) {
            mv.addObject("errorMsg", "未入力の項目があります。");
        } else if (memberE != null) {
            mv.addObject("errorMsg", "メールアドレスが既に登録されています。");
        } else {
            memberRepository.save(new Member(memberName, memberAddress, memberTel,
                                            memberEmail, memberBirth, strDate, memberPass));
            mv.addObject("member", memberRepository.findAll());
            mv.addObject("addOk", "新規登録が完了しました。");
        }
        mv.setViewName(memberE != null ? "addUser" : "login");
        return mv;
    }
}