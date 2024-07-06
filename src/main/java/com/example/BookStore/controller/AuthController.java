package com.example.BookStore.controller;

import com.example.BookStore.dao.RoleRepository;
import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.Role;
import com.example.BookStore.model.User;
import com.example.BookStore.model.UserRegReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegReq userRequest) {
        if(userRepository.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("User Role not set"));
        user.getRoles().add(userRole);

        System.out.println(user.getRoles());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
