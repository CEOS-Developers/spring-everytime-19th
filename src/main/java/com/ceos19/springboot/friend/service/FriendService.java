package com.ceos19.springboot.friend.service;

import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.friend.entity.Friend;
import com.ceos19.springboot.friend.repository.FriendRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    // 특정 유저에게 친구신청을 거는 것
    @Transactional
    public ApiResponseDto<SuccessResponse> registerFriend(UserDetails loginUser, Long userId) {

        User user1;
        user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // friendship이 만들어져야한다
        // 무조건 거는 사람이 user1, 받는 사람이 user2로 되도록
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // 만약에 user1과 2가 존재한다면 끝
        Optional<Friend> friendShip_check = friendRepository.findByUser1AndUser2(user1, user2);
        Friend friendShip = new Friend();
        if (friendShip_check.isPresent()) {
            throw new RestApiException(ErrorType.ALREADY_EXIST);
        } else {
            // 친구 관계가 존재하지 않는 경우, 새로운 친구 관계 생성

            friendShip.setUser2(user2);
            friendShip.setUser1(user1);
            friendShip.setUserStatus(false);
            friendRepository.save(friendShip);
        };
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"));
    }



    @Transactional
    public ApiResponseDto<SuccessResponse> responseFriend(UserDetails loginUser, Long userId) {
        System.out.println(loginUser);
        System.out.println(userId);
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        System.out.println(user1);
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        System.out.println(user2);
        Optional<Friend> friendShip_check = friendRepository.findByUser1AndUser2(user1, user2);
        Friend friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND));
        System.out.println(friendShip);
        friendShip.setUserStatus(true);
        friendRepository.save(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구수락이 완료되었습니다"));

    }
    @Transactional
    public ApiResponseDto<SuccessResponse> deleteFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Friend> friendShip_check = friendRepository.findByUser1AndUser2(user1, user2);
        Friend friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND));
        friendRepository.delete(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"));

    }
}
