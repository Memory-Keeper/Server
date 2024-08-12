package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.Service.CognitiveTrainingResultService;
import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cognitive-training")
public class CognitiveTrainingController {

    @Autowired
    private CognitiveTrainingResultService cognitiveTrainingResultService;

    @PostMapping("/submit")
    public ResponseEntity<CognitiveTrainingResult> submitQuizResult(
            @RequestParam String username,
            @RequestParam int memoryScore,
            @RequestParam int languageScore,
            @RequestParam int spatialAbilityScore,
            @RequestParam int executiveFunctionScore,
            @RequestParam int attentionScore,
            @RequestParam int orientationScore
    ) {
        CognitiveTrainingResult savedResult = cognitiveTrainingResultService.saveQuizResult(username, memoryScore, languageScore, spatialAbilityScore, executiveFunctionScore, attentionScore, orientationScore);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CognitiveTrainingResult>> getQuizResultsByUsername(@PathVariable String username) {
        List<CognitiveTrainingResult> results = cognitiveTrainingResultService.getQuizResultsByUsername(username);
        return ResponseEntity.ok(results);
    }
}


