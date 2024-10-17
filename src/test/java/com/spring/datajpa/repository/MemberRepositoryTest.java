package com.spring.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.spring.datajpa.dto.MemberDto;
import com.spring.datajpa.entity.Member;
import com.spring.datajpa.entity.Team;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember).isEqualTo(saveMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(findMember1);
        memberRepository.delete(findMember2);
        long count2 = memberRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 20, null);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("BBB", 15);

        assertThat(findMember.get(0).getUsername()).isEqualTo("BBB");
        assertThat(findMember.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void testNamedQuery() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> findMemberA = memberRepository.findByUsername("memberA");

        assertThat(findMemberA.get(0)).isEqualTo(memberA);
    }

    @Test
    void testSpringDataJpaNamedQuery() {
        Member memberA = new Member("memberA", 20, null);
        Member memberB = new Member("memberB", 10, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> findMemberA = memberRepository.findUser("memberA", 20);

        assertThat(findMemberA.get(0)).isEqualTo(memberA);
    }

    @Test
    void findUsernameList() {
        Member memberA = new Member("memberA", 20, null);
        Member memberB = new Member("memberB", 10, null);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> usernameList = memberRepository.findUsernameList();

        assertThat(usernameList)
                .isNotNull()
                .hasSize(2)
                .contains("memberA", "memberB");
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member memberA = new Member("memberA", 20, team);
        memberRepository.save(memberA);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        memberDto.forEach(dto -> {
            System.out.println(dto.getUsername());
            System.out.println(dto.getTeamName());
        });

        assertThat(memberDto)
                .isNotNull()
                .hasSize(1);

        assertThat(memberDto.get(0))
                .extracting(MemberDto::getUsername, MemberDto::getTeamName)
                .containsExactly("memberA", "teamA");
    }

    @Test
    void findByNames() {
        Member memberA = new Member("memberA", 20, null);
        Member memberB = new Member("memberB", 10, null);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"), 20);
        for (Member member : byNames) {
            System.out.println(member);
        }
    }
}
