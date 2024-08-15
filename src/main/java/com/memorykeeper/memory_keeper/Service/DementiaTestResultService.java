package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.DementiaTestResult;
import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.repository.DementiaTestResultRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DementiaTestResultService {

    @Autowired
    private DementiaTestResultRepository dementiaTestResultRepository;

    @Autowired
    private UserRepository userRepository;

    public DementiaTestResult saveTestResult(Long userId, int score) {
        boolean isDementiaSuspected = score >= 6;

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        int testAttempt = getNextTestAttempt(userId);

        DementiaTestResult testResult = new DementiaTestResult();
        testResult.setUserId(userId);
        testResult.setUsername(user.getUsername()); // 필요에 따라 사용
        testResult.setScore(score);
        testResult.setDementiaSuspected(isDementiaSuspected);
        testResult.setTestAttempt(testAttempt);
        testResult.setTestDate(LocalDate.now());

        return dementiaTestResultRepository.save(testResult);
    }

    public List<DementiaTestResult> getTestResultsByUserId(Long userId) {
        return dementiaTestResultRepository.findByUserId(userId);
    }

    private int getNextTestAttempt(Long userId) {
        List<DementiaTestResult> userResults = dementiaTestResultRepository.findByUserId(userId);
        return userResults.size() + 1;
    }
}



