package com.example.hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @Column(name = "member_id")
    private int memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_address")
    private String memberAddress;

    @Column(name = "member_tel")
    private String memberTel;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_birth")
    private String memberBirth;

    @Column(name = "member_join")
    private String memberJoin;

    @Column(name = "member_withdrawal")
    private String memberWithdrawal;

    @Column(name = "member_password")
    private String memberPass;

    public Member() {

    }

    public Member(int memberId, String memberName, String memberAddress, String memberTel,
                  String memberEmail, String memberBirth, String memberJoin, String memberPass) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberAddress = memberAddress;
        this.memberTel = memberTel;
        this.memberEmail = memberEmail;
        this.memberBirth = memberBirth;
        this.memberJoin = memberJoin;
        this.memberPass = memberPass;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getMemberBirth() {
        return memberBirth;
    }

    public String getMemberJoin() {
        return memberJoin;
    }

    public String getMemberWithdrawal() {
        return memberWithdrawal;
    }

    public String getMemberPass() {
        return memberPass;
    }
}
