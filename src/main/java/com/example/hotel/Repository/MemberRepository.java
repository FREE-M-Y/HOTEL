package com.example.hotel.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotel.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByMemberEmailAndMemberPass(String memberEmail,String memberPass);
    Member findByMemberEmail(String memberEmail);
    List<Member> findByMemberId(int memberId);
    Member findByMemberWithdrawal(String memberWithdrawal);
    List<Member> findByMemberNameLike(String MemberName);
    List<Member> findByMemberAddressLike(String MemberAddress);

}