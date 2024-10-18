package com.spring.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.spring.datajpa.entity.Member;

@SpringBootTest
@Transactional
public class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(saveMember.getId());
        
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember).isEqualTo(saveMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(findMember1);
        memberJpaRepository.delete(findMember2);
        long count2 = memberJpaRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    void testNamedQuery() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);
        
        List<Member> findMemberA = memberJpaRepository.findByUsername("memberA");

        assertThat(findMemberA.get(0)).isEqualTo(memberA);
    }

    @Test
    void paging() {
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 10, null));
        memberJpaRepository.save(new Member("member3", 10, null));
        memberJpaRepository.save(new Member("member4", 10, null));
        memberJpaRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        Long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void testAgePlus() {
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 15, null);
        Member memberC = new Member("memberC", 20, null);

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);
        memberJpaRepository.save(memberC);

        int count = memberJpaRepository.bulkAgePlus(15);

        assertThat(count).isEqualTo(2);
    }
}
