package com.ceos19.springeverytime.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {



    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout() {

        return ResponseEntity.ok().build();
    }
}
