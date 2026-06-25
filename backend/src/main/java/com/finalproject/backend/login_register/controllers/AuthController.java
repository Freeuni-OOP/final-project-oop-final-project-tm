package com.finalproject.backend.login_register.controllers;

import com.finalproject.backend.login_register.DTO.LoginRequest;
import com.finalproject.backend.login_register.DTO.RegisterRequest;
import com.finalproject.backend.entities.Users;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Users> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        System.out.println("works??");
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        Users user = userOptional.get();
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassHash());
        System.out.println("Valid DB Hash for 123: " + passwordEncoder.encode("123"));
        System.out.println("Valid DB Hash for 124: " + passwordEncoder.encode("124"));
        System.out.println("Valid DB Hash for 1: " + passwordEncoder.encode("1"));
        if (!matches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(user);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered Email is already in use");
        }
        Users user = new Users();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirst_name());
        user.setLastName(registerRequest.getLast_name());

        String passHash = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassHash(passHash);

        Users savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

}