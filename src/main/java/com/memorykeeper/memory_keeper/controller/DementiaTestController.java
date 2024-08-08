package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.DementiaTestResult;
import com.memorykeeper.memory_keeper.repository.DementiaTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dementia-test")
public class DementiaTestController {

    @Autowired
    private DementiaTestResultRepository dementiaTestResultRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitTestResult(@RequestParam String username, @RequestParam int score) {
        boolean isDementiaSuspected = score >= 6;

        DementiaTestResult result = new DementiaTestResult();
        result.setUsername(username);
        result.setScore(score);
        result.setDementiaSuspected(isDementiaSuspected);

        dementiaTestResultRepository.save(result);

        return ResponseEntity.ok("Test result submitted successfully!");
    }
}
