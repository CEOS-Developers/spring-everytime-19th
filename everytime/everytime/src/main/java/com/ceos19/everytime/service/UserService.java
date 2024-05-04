package com.ceos19.everytime.service;



import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.dto.AddUserRequest;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.exception.NotFoundException;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .loginPassword(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getUserId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));
    }

}
