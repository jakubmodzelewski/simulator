package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.dto.AuthenticationResponse;
import com.jmodzelewski.simulator.dto.LoginRequest;
import com.jmodzelewski.simulator.dto.RegisterRequest;
import com.jmodzelewski.simulator.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authenticationService.signUp(registerRequest);
        return new ResponseEntity<>("User registered succesfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

}
