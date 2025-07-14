package com.sprint.mission.springjwt.service;

import com.sprint.mission.springjwt.dto.JoinDto;
import com.sprint.mission.springjwt.entity.User;
import com.sprint.mission.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(JoinDto joinDto) {
        String username = joinDto.getUsername();
        String encodedPassword = passwordEncoder.encode(joinDto.getPassword());

        if(userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }
}
