package com.example.ordersystem.member.domain;

import com.example.ordersystem.common.BaseTimeEntity;
import com.example.ordersystem.member.dtos.MemberResDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role=Role.USER;


    public MemberResDto toListDto(){
        return MemberResDto.builder().id(this.id).name(this.name).email(this.email).build();
    }

//    public MemberResDto toMyListResDTo(){
//
//    }


}
