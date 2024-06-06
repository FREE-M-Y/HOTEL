package com.example.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByMemberEmailAndMemberPass(String memberEmail,String memberPass);
    Member findByMemberEmail(String memberEmail);
    Member findByMemberId(int memberId);

}