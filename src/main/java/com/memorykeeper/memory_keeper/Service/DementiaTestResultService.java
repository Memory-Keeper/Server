package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.DementiaTestResult;
import com.memorykeeper.memory_keeper.repository.DementiaTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DementiaTestResultService {

    @Autowired
    private DementiaTestResultRepository dementiaTestResultRepository;

    public DementiaTestResult saveTestResult(String username, int score) {
        // 점수가 6점 이상이면 치매 의심으로 설정
        boolean isDementiaSuspected = score >= 6;

        // 기존에 저장된 시행 회차를 조회하여 다음 회차 번호를 설정
        int testAttempt = getNextTestAttempt(username);

        DementiaTestResult testResult = new DementiaTestResult();
        testResult.setUsername(username);
        testResult.setScore(score);
        testResult.setDementiaSuspected(isDementiaSuspected);
        testResult.setTestAttempt(testAttempt);
        testResult.setTestDate(LocalDate.now());

        return dementiaTestResultRepository.save(testResult);
    }

    public List<DementiaTestResult> getTestResultsByUsername(String username) {
        return dementiaTestResultRepository.findByUsername(username);
    }

    private int getNextTestAttempt(String username) {
        List<DementiaTestResult> userResults = dementiaTestResultRepository.findByUsername(username);
        return userResults.size() + 1;
    }
}

