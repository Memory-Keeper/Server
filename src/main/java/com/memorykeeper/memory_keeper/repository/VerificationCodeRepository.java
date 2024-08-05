package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByPhoneNumberAndCode(String phoneNumber, String code);
}