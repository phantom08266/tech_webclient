package com.example.tech_webclient.controller.dto;

import com.example.tech_webclient.domain.Member;
import lombok.Builder;

import java.util.List;

@Builder
public class TotalMemberResponse {
    public List<MemberDto> members;
    public int memberTotalCount;

//    public static Object of(List<Member> members, int size) {
//        return TotalMemberResponse.builder()
//                .members(members)
//                .memberTotalCount(size)
//                .build();
//    }
}
