package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.database.UserRepository;
import com.jmodzelewski.simulator.dto.*;
import com.jmodzelewski.simulator.model.Simulation;
import com.jmodzelewski.simulator.model.User;
import com.jmodzelewski.simulator.security.JWTProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final SimulationService simulationService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public SimulationDTO saveSimulation(String username, SimulationDTO simulationDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User " + username + " not found."));
        Simulation simulation = simulationService.mapDTOtoSimulation(simulationDTO);

        user.getSimulations().stream().filter(sim -> sim.getId().equals(simulation.getId())).findFirst().ifPresentOrElse(sim -> sim = simulation, () -> user.getSimulations().add(simulation));

        user.setLastUsedSimulationId(simulationDTO.getId());
        userRepository.save(user);
        return simulationDTO;
    }

    @Transactional
    public List<SimulationDTO> getUserSimulations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User " + username + " not found."));
        return user.getSimulations().stream().map(simulationService::mapSimulationToDTO).collect(Collectors.toList());
    }

    @Transactional
    public SimulationDTO getUserSimulation(Long id) {
        return simulationService.load(id);
    }

    @Transactional
    public Long getUserLastSimulationId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User " + username + " not found."));
        return user.getLastUsedSimulationId();
    }
}
