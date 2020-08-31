package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.dto.*;
import com.jmodzelewski.simulator.services.SimulationService;
import com.jmodzelewski.simulator.services.UserService;
import com.jmodzelewski.simulator.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final SimulationService simulationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        userService.signUp(registerRequest);
        return new ResponseEntity<>("User registered succesfully!", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh token deleted successfully.");
    }

    @PostMapping("/simulation/{username}")
    public ResponseEntity<String> saveSimulation(@PathVariable String username, @RequestBody SimulationDTO simulationDTO) {
        simulationService.save(simulationDTO);
        userService.saveSimulation(username, simulationDTO);
        return new ResponseEntity<>("Simulation saved successfully!", OK);
    }

    @GetMapping("/simulation/{username}")
    public  ResponseEntity<List<SimulationDTO>> getAllUserSimulations(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserSimulations(username));
    }
}
