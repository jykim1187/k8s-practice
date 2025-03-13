package com.example.ordersystem.member.service;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.dtos.LoginDto;
import com.example.ordersystem.member.dtos.MemberResDto;
import com.example.ordersystem.member.dtos.MemberSaveReqDto;
import com.example.ordersystem.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    회원가입
    public Long save(MemberSaveReqDto memberSaveReqDto){
//      이메일 중복확인하는 거 추가할 것!
        if(memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()){
            throw new  IllegalArgumentException("this email already exist");
        }

        Member member = memberSaveReqDto.toEntity(passwordEncoder.encode(memberSaveReqDto.getPassword()));
        memberRepository.save(member);
        return member.getId();
    }
//    회원 목록 조회
    public List<MemberResDto> findAll(){
       return memberRepository.findAll().stream().map(m->m.toListDto()).toList();//Collec(Collector.toList()대신 사용가능)

    }

//    로그인
    public Member login(LoginDto dto){

        boolean check = true;
//        email존재여부
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
        if(!optionalMember.isPresent()){
            check = false;
        }
//        password일치 여부
        if(!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())){
            check =false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalMember.get();
    }

//    내 정보 조회
//    public MemberResDto findByEmail(String email){
//    Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("the email does not exist"));
//    return  member.toListDto();
//    }
    public MemberResDto myInfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("the email does not exist"));
        return member.toListDto();
    }










}
