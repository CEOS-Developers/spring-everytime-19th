package com.ceos19.everytime.service;



import com.ceos19.everytime.config.jwt.TokenProvider;
import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.dto.AddUserRequest;
import com.ceos19.everytime.dto.LoginResponseDTO;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.exception.NotFoundException;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;


    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.email())
                .loginPassword(bCryptPasswordEncoder.encode(dto.password()))
                .build()).getUserId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));
    }


    public LoginResponseDTO loginUser(Long userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));

       //패스워드 비교하여 일치여부 판단
        if(!bCryptPasswordEncoder.matches(password, user.getLoginPassword())) throw new RuntimeException();

        //LoginResponseDTO 객체 생성
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .token(tokenProvider.createToken(String.valueOf(user.getUserId()))).build();

        return loginResponseDTO;
    }
}
