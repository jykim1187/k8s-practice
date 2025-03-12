package com.example.ordersystem.member.dtos;

import com.example.ordersystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberSaveReqDto {
    private String name;
    private String email;
    private String password;


    public Member toEntity(String encodedPassword){
        return Member.builder().name(this.name).email(this.email).password(encodedPassword).build();
    }

}
