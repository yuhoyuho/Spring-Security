package com.example.TestSecurity.service;

import com.example.TestSecurity.dto.JoinDto;
import com.example.TestSecurity.repository.UserRepository;
import com.example.TestSecurity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(JoinDto joinDto) {
        boolean isUser = userRepository.existsByUsername(joinDto.getUsername());
        if(isUser) {
            return;
        }

        User user = new User();
        user.setUsername(joinDto.getUsername());
        user.setPassword(passwordEncoder.encode(joinDto.getPassword()));

        if(user.getUsername().equals("admin")) {
            user.setRole("ROLE_ADMIN");
        }
        else {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);
    }
}
