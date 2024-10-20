package com.spring.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.datajpa.dto.MemberDto;
import com.spring.datajpa.entity.Member;
import com.spring.datajpa.repository.MemberRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    /**
     * ?page=0&size=7&sort=id,desc&sort=name,desc
     * page: 현재 페이지, 0부터 시작
     * size: 한 페이지에 노출할 데이터 건수
     * sort: 정렬 조건 [ASC(기본값)|DESC], 파라미터 추가 가능
     * 
     * 추가...
     * - 기본값 page size 20... application.yml 글로벌 설정 가능 or @PageableDefault 개별 설정 가능
     * - @Qualifier 접두사명 추가 가능 ?member_page=1&sort=...
     */
    @GetMapping("/members")
    public Page<MemberDto> members(@PageableDefault(size = 5, sort = "age") Pageable pageable
    /* , @Qualifier("member") Pageable memberPageable */ ) {
        Page<Member> pageByAge = memberRepository.findPageBy(pageable);
        Page<MemberDto> map = pageByAge.map(member -> new MemberDto(member.getId(), member.getUsername(), ""));
        return map;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 40; i++) {
            memberRepository.save(new Member("user" + i, i, null));
        }
    }

}
