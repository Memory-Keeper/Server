package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.VerificationCode;
import com.memorykeeper.memory_keeper.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private SmsService smsService;

    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_MINUTES = 5;

    public void sendVerificationCode(String phoneNumber) {
        String code = generateCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(expirationTime);

        verificationCodeRepository.save(verificationCode);

        // 이제 발신번호는 SmsService 내에서 관리됩니다.
        smsService.sendSms(phoneNumber, "Your verification code is: " + code);
    }

    public boolean verifyCode(String phoneNumber, String code) {
        Optional<VerificationCode> optionalVerificationCode = verificationCodeRepository.findByPhoneNumberAndCode(phoneNumber, code);

        if (optionalVerificationCode.isPresent()) {
            VerificationCode verificationCode = optionalVerificationCode.get();
            return verificationCode.getExpirationTime().isAfter(LocalDateTime.now());
        }
        return false;
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
