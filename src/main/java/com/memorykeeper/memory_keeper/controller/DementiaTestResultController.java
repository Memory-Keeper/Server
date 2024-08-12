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
    public ResponseEntity<DementiaTestResult> saveTestResult(@RequestParam String username, @RequestParam int score) {
        DementiaTestResult savedResult = dementiaTestResultService.saveTestResult(username, score);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<DementiaTestResult>> getTestResultsByUsername(@PathVariable String username) {
        List<DementiaTestResult> results = dementiaTestResultService.getTestResultsByUsername(username);
        return ResponseEntity.ok(results);
    }
}

