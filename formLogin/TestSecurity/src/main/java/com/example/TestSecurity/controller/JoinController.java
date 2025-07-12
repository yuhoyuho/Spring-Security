package com.example.TestSecurity.controller;

import com.example.TestSecurity.dto.JoinDto;
import com.example.TestSecurity.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @GetMapping("/join")
    public String joinP() {
        return "join";
    }

    @PostMapping("/join")
    public String join(JoinDto joinDto) {
        joinService.join(joinDto);
        return "redirect:/login";
    }
}
