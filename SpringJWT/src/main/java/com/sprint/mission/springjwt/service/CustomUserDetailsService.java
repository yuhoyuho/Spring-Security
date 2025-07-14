package com.sprint.mission.springjwt.service;

import com.sprint.mission.springjwt.dto.CustomUserDetails;
import com.sprint.mission.springjwt.entity.User;
import com.sprint.mission.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user != null) {
            return new CustomUserDetails(user);
        }

        return null;
    }
}
