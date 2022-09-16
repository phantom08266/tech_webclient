package com.example.tech_webclient.controller;

import com.example.tech_webclient.controller.dto.TotalMemberResponse;
import com.example.tech_webclient.domain.Member;
import com.example.tech_webclient.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final WebClient customWebClient;
    private final RestTemplate customRestTemplate;

    @GetMapping("/delay/{time}")
    public void delay(@PathVariable int time) {
        customWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/delay/{time}").build(10000))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException(">>>>>>>>>>>> API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException(">>>>>>>>>>>> Server is not responding")))
                .bodyToMono(String.class)
                .subscribe();
    }

    @GetMapping("/delay-blocking/{time}")
    public void delay2(@PathVariable int time) {
        Map<String, String> param = new HashMap<>();
        param.put("time", "10000");

        customRestTemplate.getForObject("http://localhost:9999/delay/{time}", String.class, param);
    }
}
