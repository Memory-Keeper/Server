package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import com.memorykeeper.memory_keeper.Service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String phoneNumber) {
        verificationService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Verification code sent successfully!");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean isVerified = verificationService.verifyCode(phoneNumber, code);
        if (isVerified) {
            return ResponseEntity.ok("Verification successful!");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestParam String code) {
        boolean isVerified = verificationService.verifyCode(user.getPhoneNumber(), code);
        if (!isVerified) {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
}