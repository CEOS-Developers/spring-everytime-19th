package com.ceos19.springboot.users.service;

import com.ceos19.springboot.users.UserDetailsImpl;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {		// 1.
    private final UserRepository userRepository;

    // DB 에 저장된 사용자 정보와 일치하는지 여부를 판단
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user);
    }
}
