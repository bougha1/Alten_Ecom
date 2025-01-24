package com.example.ecommerce.controller;

import com.example.ecommerce.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/token")
    public String generateToken(@RequestBody Map<String, String> authRequest) {
        String username = authRequest.get("username");
        String password = authRequest.get("password");

        // Simuler un utilisateur admin pour la démonstration
        if ("admin".equals(username) && passwordEncoder.matches(password, passwordEncoder.encode("admin123"))) {
            return jwtTokenUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
