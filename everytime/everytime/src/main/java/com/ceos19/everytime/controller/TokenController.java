package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.AccessTokenRequestDTO;
import com.ceos19.everytime.dto.AccessTokenResponseDTO;
import com.ceos19.everytime.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@Getter
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<AccessTokenResponseDTO> createNewAccessToken (@RequestBody AccessTokenRequestDTO request) {
        String newAccessToken = tokenService.createNewAccessToken(request.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AccessTokenResponseDTO(newAccessToken));
    }
}
