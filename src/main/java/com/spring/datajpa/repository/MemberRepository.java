package com.spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
