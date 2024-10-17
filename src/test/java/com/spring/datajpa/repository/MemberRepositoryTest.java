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
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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

}
