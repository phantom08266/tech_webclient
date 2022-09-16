package com.example.tech_webclient.controller;

import com.example.tech_webclient.controller.dto.TotalMemberResponse;
import com.example.tech_webclient.domain.Member;
import com.example.tech_webclient.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<TotalMemberResponse> findAllMember() {
        List<Member> members =  memberService.findAllMember();
        return ResponseEntity.ok(TotalMemberResponse.of(members, members.size()));
    }
}
