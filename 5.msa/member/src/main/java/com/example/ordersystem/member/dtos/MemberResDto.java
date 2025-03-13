package com.example.ordersystem.member.dtos;

import com.example.ordersystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberResDto {
    private Long id;
    private String name;
    private String email;
    private Integer orderCount;






}
