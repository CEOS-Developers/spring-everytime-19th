package com.ceos19.springeverytime.domain.user.service;

import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.dto.response.CustomUserDetails;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import com.ceos19.springeverytime.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username).orElseThrow(
                () -> new BadRequestException(ExceptionCode.NOT_FOUND_LOGIN_ID)
        );
        return new CustomUserDetails(user);
    }
}
