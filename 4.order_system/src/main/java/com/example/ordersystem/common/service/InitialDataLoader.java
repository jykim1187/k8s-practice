package com.example.ordersystem.common.service;


import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.domain.Role;
import com.example.ordersystem.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner를 상속함으로써 해당 컴포넌트가 스프링빈으로 등록되는 시점에서 바로 run메서드 자동실행
//프로그램을 실행하면 가장 먼저 실행되는 클래스라고 보면 된다.
@Component
public class InitialDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(memberRepository.findByEmail("admin@naver.com").isPresent())return;
        //실행할 때 마다 동일한 어드민계정을 만들면 동일한 이메일로 에러가 날꺼니까
        Member member  = Member.builder()
                .name("admin")
                .email("admin@naver.com")
                .password(passwordEncoder.encode("12341234"))
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);
    }
}
