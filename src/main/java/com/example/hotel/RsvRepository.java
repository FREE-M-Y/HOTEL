package com.example.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsvRepository extends JpaRepository<Rsv, Integer> {
    
}
