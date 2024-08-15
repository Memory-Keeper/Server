package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.DementiaTestResult;
import com.memorykeeper.memory_keeper.Service.DementiaTestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dementia-test-results")
public class DementiaTestResultController {

    @Autowired
    private DementiaTestResultService dementiaTestResultService;

    @PostMapping("/save")
    public ResponseEntity<DementiaTestResult> saveTestResult(@RequestParam Long userId, @RequestParam int score) {
        DementiaTestResult savedResult = dementiaTestResultService.saveTestResult(userId, score);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DementiaTestResult>> getTestResultsByUserId(@PathVariable Long userId) {
        List<DementiaTestResult> results = dementiaTestResultService.getTestResultsByUserId(userId);
        return ResponseEntity.ok(results);
    }
}



