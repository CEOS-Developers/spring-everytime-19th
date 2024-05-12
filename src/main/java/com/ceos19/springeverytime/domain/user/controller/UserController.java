package com.ceos19.springeverytime.domain.user.controller;

import com.ceos19.springeverytime.domain.user.dto.request.UserCreateRequest;
import com.ceos19.springeverytime.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid final UserCreateRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.delete(auth);
        return ResponseEntity.noContent().build();
    }
}
