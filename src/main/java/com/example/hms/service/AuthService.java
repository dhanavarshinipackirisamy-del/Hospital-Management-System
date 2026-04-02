package com.example.hms.service;

import com.example.hms.config.JwtService;
import com.example.hms.dto.AuthRequest;
import com.example.hms.entity.User;
import com.example.hms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public String login(AuthRequest request) {
        User user = repo.findByEmail(request.getEmail())
                .orElseThrow();

        if (encoder.matches(request.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user.getEmail());
        }

        throw new RuntimeException("Invalid credentials");
    }
}