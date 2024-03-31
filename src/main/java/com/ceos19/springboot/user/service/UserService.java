package com.ceos19.springboot.user.service;

import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.LoginType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.UserRoleEnum;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.common.jwt.JwtUtil;
import com.ceos19.springboot.user.dto.LoginRequestsDto;
import com.ceos19.springboot.user.dto.SignupRequestDto;
import com.ceos19.springboot.user.dto.TokenDto;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiResponseDto<SuccessResponse> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            throw new RestApiException(ErrorType.NOT_FOUND_USER);
        }

        // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
        UserRoleEnum role = requestDto.getAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
        User user = User.of(LoginType.NORMAL, username, password, role);

        userRepository.save(User.of(LoginType.NORMAL,username, password, role));
        TokenDto tokenDto = new TokenDto();
        String accessToken = jwtUtil.createAccessToken(user.getUsername() , user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

        tokenDto.setMessage("로그인 성공");
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }

    public ApiResponseDto<SuccessResponse> login(LoginRequestsDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인 & 비밀번호 확인
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new RestApiException(ErrorType.NOT_FOUND);
        }

        // header 에 들어갈 JWT 세팅
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
        String jwtToken = jwtUtil.createToken(user.get().getUsername(), user.get().getRole());
        jwtToken.substring(7);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "로그인 성공"));

    }
}
