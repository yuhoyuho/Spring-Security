package com.sprint.mission.springjwt.controller;

import com.sprint.mission.springjwt.dto.JoinDto;
import com.sprint.mission.springjwt.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String join(JoinDto joinDto) {
        joinService.join(joinDto);

        System.out.println(joinDto.getUsername() + " ,,,,,,,,,,,,," + joinDto.getPassword());
        return "OK";
    }
}
