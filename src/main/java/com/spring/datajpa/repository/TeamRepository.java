package com.spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
}
