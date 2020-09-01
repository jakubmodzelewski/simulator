package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.dto.*;
import com.jmodzelewski.simulator.services.*;
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
    private final NodeService nodeService;
    private final LinkService linkService;

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

    @PostMapping("/simulations/{username}")
    public ResponseEntity<SimulationDTO> saveSimulation(@PathVariable String username, @RequestBody SimulationDTO simulationDTO) {
        for (NodeDTO nodeDTO : simulationDTO.getNodes()) {
            nodeService.save(nodeDTO);
        }

        for (LinkDTO linkDTO : simulationDTO.getLinks()) {
            linkService.save(linkDTO);
        }

        simulationService.save(simulationDTO);
        userService.saveSimulation(username, simulationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulationDTO);
    }

    @GetMapping("/simulations/{username}")
    public  ResponseEntity<List<SimulationDTO>> getAllUserSimulations(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserSimulations(username));
    }

    @GetMapping("/simulation/{id}")
    public ResponseEntity<SimulationDTO> getUserSimulation(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserSimulation(id));
    }

    @GetMapping("/simulationId/{username}")
    public ResponseEntity<Long> getUserLastSimulation(@PathVariable String username ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserLastSimulationId(username));
    }
}
