package com.library.controller;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // Constructor for dependency injection (instead of @RequiredArgsConstructor)
    public AuthController(UserRepository userRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        User user = new User();
        user.setName(payload.get("name"));
        user.setEmail(payload.get("email"));
        user.setPassword(passwordEncoder.encode(payload.get("password")));
        user.setRoles(Set.of("ROLE_PATRON"));

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        User user = userRepository.findByEmail(payload.get("email"))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(payload.get("password"), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
