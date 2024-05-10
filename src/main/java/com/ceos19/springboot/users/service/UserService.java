package com.ceos19.springboot.users.service;

import com.ceos19.springboot.common.code.ErrorCode;
import com.ceos19.springboot.global.exception.BusinessExceptionHandler;
import com.ceos19.springboot.jwt.TokenProvider;
import com.ceos19.springboot.users.domain.UserRoleEnum;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.users.dto.UserRequestDto;
import com.ceos19.springboot.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Users createUser(UserRequestDto userRequestDto) {
        Users user = Users.builder()
                .nickname(userRequestDto.getNickname())
                .username(userRequestDto.getLoginId())
                .loginId(userRequestDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
                .email("")
                .role(UserRoleEnum.USER)
                .build();
        return userRepository.save(user);
    }

    public Users findUser(Long userId) {
       return userRepository.findById(userId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
    }

    @Transactional
    public void deleteUser(Users user) {
        /** 사용자 탈퇴 이전에 관련된 것들 모두 삭제*/
        postLikeRepository.deleteByUser(user);
        postRepository.deleteByUser(user);
        commentRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    public Users findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
    }

    public boolean validateDuplicateUser(UserRequestDto userRequestDto) {
        Optional<Users> findUser = userRepository.findByLoginId(userRequestDto.getLoginId());
        if (findUser.isPresent()) {
            throw new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_ERROR);
        } else
            return true;
    }
}
