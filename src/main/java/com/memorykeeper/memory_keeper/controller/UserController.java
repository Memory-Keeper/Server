package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import com.memorykeeper.memory_keeper.Service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // 비밀번호 재설정 요청 (인증 코드 전송)
    @PostMapping("/reset-password-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestParam String username, @RequestParam String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty() || !optionalUser.get().getPhoneNumber().equals(phoneNumber)) {
            return ResponseEntity.status(404).body("User not found or phone number does not match.");
        }

        verificationService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Verification code sent successfully!");
    }

    // 비밀번호 재설정 (인증 코드 검증 및 비밀번호 변경)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String username, @RequestParam String phoneNumber,
                                           @RequestParam String code, @RequestParam String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty() || !optionalUser.get().getPhoneNumber().equals(phoneNumber)) {
            return ResponseEntity.status(404).body("User not found or phone number does not match.");
        }

        boolean isVerified = verificationService.verifyCode(phoneNumber, code);
        if (!isVerified) {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 암호화 후 변경
        userRepository.save(user);
        return ResponseEntity.ok("Password reset successfully!");
    }

    // 아이디 찾기 요청 (인증 코드 전송)
    @PostMapping("/find-username-request")
    public ResponseEntity<?> findUsernameRequest(@RequestParam String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User with the given phone number not found.");
        }

        verificationService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Verification code sent successfully!");
    }

    // 아이디 찾기 (인증 코드 검증 후 아이디 반환)
    @PostMapping("/find-username")
    public ResponseEntity<?> findUsername(@RequestParam String phoneNumber, @RequestParam String code) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User with the given phone number not found.");
        }

        boolean isVerified = verificationService.verifyCode(phoneNumber, code);
        if (!isVerified) {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }

        User user = optionalUser.get();
        return ResponseEntity.ok("Your username is: " + user.getUsername());
    }
}

