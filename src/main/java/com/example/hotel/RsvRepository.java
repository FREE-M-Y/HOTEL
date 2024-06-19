package com.example.hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RsvRepository extends JpaRepository<Rsv, Integer> {
    List<Rsv> findByMemberId(int memberId);
    Rsv findByRsvId(int rsvId);

    @Transactional
    void deleteByRsvId(int rsvId);
}