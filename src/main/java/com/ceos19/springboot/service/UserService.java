package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Users;
import com.ceos19.springboot.repository.CommentRepository;
import com.ceos19.springboot.repository.PostLikeRepository;
import com.ceos19.springboot.repository.PostRepository;
import com.ceos19.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long saveUser(Users user) {
        Users saveUser = userRepository.save(user);
        return saveUser.getUserId();
    }

    @Transactional
    public void deleteUser(Users user) {
        /** 사용자 탈퇴 이전에 관련된 것들 모두 삭제*/
        postLikeRepository.deleteByUser(user);
        postRepository.deleteByUser(user);
        commentRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
