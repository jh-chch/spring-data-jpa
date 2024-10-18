package com.spring.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.spring.datajpa.dto.MemberDto;
import com.spring.datajpa.entity.Member;
import com.spring.datajpa.entity.Team;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

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

    @Test
    void paging() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 0; // page 인덱스 0부터 시작
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username"));

        // Slice는 count 쿼리가 안나가고, +1만큼 더 가져온다. 예) 더보기버튼 -> +1 가져온 데이터는 숨겨두고 버튼 눌렀을 때 ...
        // Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // 반환타입을 List로 받아도 PageRequest 적용됨
        // List<Member> page = memberRepository.findListByAge(age, pageRequest);

        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        // API로 응답할 때, DTO로 변환하여 응답할 때 map()을 이용해서 편리하게 변환할 수 있다.
        Page<MemberDto> pageMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> members = page.getContent();
        long totalCount = page.getTotalElements();

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    void testAgePlus() {
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 15, null);
        Member memberC = new Member("memberC", 20, null);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        int count = memberRepository.bulkAgePlus(15);

        /**
         * memberC의 age를 bulkAgePlus된 21로 생각할 수 있다.
         * 하지만 bulk 연산은 DB에는 반영됐지만, 영속성 컨텍스트에는 반영되지 않았다.
         * 따라서, em.clear()를 호출해야한다.
         */
        List<Member> result = memberRepository.findByUsername("memberC");
        Member findMemberC = result.get(0);
        System.out.println(findMemberC); // 20

        // em.clear(); // @Modifying의 clearAutomatically = true 옵션으로 대체

        List<Member> result2 = memberRepository.findByUsername("memberC");
        Member findMemberC2 = result2.get(0);
        System.out.println(findMemberC2); // 21

        assertThat(count).isEqualTo(2);
    }
}
