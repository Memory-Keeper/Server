package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;
import com.memorykeeper.memory_keeper.repository.CognitiveTrainingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cognitive-training")
public class CognitiveTrainingController {

    @Autowired
    private CognitiveTrainingResultRepository cognitiveTrainingResultRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuizResult(
            @RequestParam String username,
            @RequestParam int memoryScore,
            @RequestParam int languageScore,
            @RequestParam int spatialAbilityScore,
            @RequestParam int executiveFunctionScore,
            @RequestParam int attentionScore,
            @RequestParam int orientationScore
    ) {
        // 유효성 검사 추가
        if (memoryScore < 0 || languageScore < 0 || spatialAbilityScore < 0 || executiveFunctionScore < 0 || attentionScore < 0 || orientationScore < 0) {
            return ResponseEntity.badRequest().body("Scores must be non-negative integers.");
        }

        int totalScore = memoryScore + languageScore + spatialAbilityScore + executiveFunctionScore + attentionScore + orientationScore;
        double percentile = calculatePercentile(totalScore);

        CognitiveTrainingResult result = new CognitiveTrainingResult();
        result.setUsername(username);
        result.setMemoryScore(memoryScore);
        result.setLanguageScore(languageScore);
        result.setSpatialAbilityScore(spatialAbilityScore);
        result.setExecutiveFunctionScore(executiveFunctionScore);
        result.setAttentionScore(attentionScore);
        result.setOrientationScore(orientationScore);
        result.setTotalScore(totalScore);
        result.setPercentile(percentile);

        cognitiveTrainingResultRepository.save(result);

        return ResponseEntity.ok("Quiz result submitted successfully!");
    }

    private double calculatePercentile(int totalScore) {
        List<CognitiveTrainingResult> allResults = cognitiveTrainingResultRepository.findAll();
        int totalUsers = allResults.size();

        if (totalUsers == 0) {
            return 100.0; // 사용자가 첫 번째일 때는 상위 100%로 설정
        }

        long rank = allResults.stream()
                .filter(result -> result.getTotalScore() > totalScore)
                .count() + 1;

        return (double) rank / totalUsers * 100;
    }
}

