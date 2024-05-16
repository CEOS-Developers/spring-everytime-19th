package com.ceos19.everytime.controller;


import com.ceos19.everytime.dto.AddUserRequest;
import com.ceos19.everytime.dto.LoginDTO;
import com.ceos19.everytime.dto.LoginResponseDTO;
import com.ceos19.everytime.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(AddUserRequest request) {
        userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDTO loginDTO) {

        LoginResponseDTO loginResponseDTO = userService.loginUser(loginDTO.userId(), loginDTO.password());
        return ResponseEntity.status(HttpStatus.OK).build();
    }




}
