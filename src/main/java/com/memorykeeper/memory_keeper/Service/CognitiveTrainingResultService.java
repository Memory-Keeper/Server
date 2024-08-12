package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;
import com.memorykeeper.memory_keeper.repository.CognitiveTrainingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CognitiveTrainingResultService {

    @Autowired
    private CognitiveTrainingResultRepository cognitiveTrainingResultRepository;

    public CognitiveTrainingResult saveQuizResult(String username, int memoryScore, int languageScore, int spatialAbilityScore, int executiveFunctionScore, int attentionScore, int orientationScore) {
        int totalScore = memoryScore + languageScore + spatialAbilityScore + executiveFunctionScore + attentionScore + orientationScore;
        double percentile = calculatePercentile(totalScore);

        // 기존에 저장된 시행 회차를 조회하여 다음 회차 번호를 설정
        int quizAttempt = getNextQuizAttempt(username);

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
        result.setQuizAttempt(quizAttempt);
        result.setQuizDate(LocalDate.now());

        return cognitiveTrainingResultRepository.save(result);
    }

    public List<CognitiveTrainingResult> getQuizResultsByUsername(String username) {
        return cognitiveTrainingResultRepository.findByUsername(username);
    }

    private int getNextQuizAttempt(String username) {
        List<CognitiveTrainingResult> userResults = cognitiveTrainingResultRepository.findByUsername(username);
        return userResults.size() + 1;
    }

    private double calculatePercentile(int totalScore) {
        List<CognitiveTrainingResult> allResults = cognitiveTrainingResultRepository.findAll();
        int totalUsers = allResults.size();

        if (totalUsers == 0) {
            return 100.0; // 첫 번째 사용자일 경우 100%로 설정
        }

        long rank = allResults.stream()
                .filter(result -> result.getTotalScore() > totalScore)
                .count() + 1;

        return (double) rank / totalUsers * 100;
    }
}

