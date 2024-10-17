package com.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") // 생략 가능 같은 이름의 NamedQuery가 있으면 해당 쿼리를 실행해주고, 없으면 쿼리를 생성해준다.
    List<Member> findByUsername(@Param("username") String username);
}
