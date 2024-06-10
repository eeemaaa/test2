package com.compscizimsecurity.compscizimsecurity.auth;

import com.compscizimsecurity.compscizimsecurity.config.JwtService;
import com.compscizimsecurity.compscizimsecurity.model.Role;
import com.compscizimsecurity.compscizimsecurity.model.User;
import com.compscizimsecurity.compscizimsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRole(Role.USER); // Назначаем роль пользователя по умолчанию
        userRepository.save(request);
        return ResponseEntity.ok().body(Map.of("response", "User added successfully"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok().body(Map.of("response", "Successfully authenticated", "token", token));
    }
}
