package com.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.datajpa.dto.MemberDto;
import com.spring.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") // 생략 가능 같은 이름의 NamedQuery가 있으면 해당 쿼리를 실행해주고, 없으면 쿼리를 생성해준다.
    List<Member> findByUsername(@Param("username") String username);

    // NamedQuery와 마찬가지로 애플리케이션 실행 시점에 문법 오류를 잡아준다.(정적쿼리)
    // 보통 직접 정의하는 것으로 사용 하고, 편리하다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 단순 값으로 직접 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO로 직접 조회
    @Query("select new com.spring.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 리스트 타입 파라미터 바인딩 테스트 (in절로 쿼리가 나간다.)
    @Query("select m from Member m where m.username in :names and m.age = :age")
    List<Member> findByNames(@Param("names") List<String> names, @Param("age") int age);

}
