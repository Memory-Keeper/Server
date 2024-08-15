package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;
import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.repository.CognitiveTrainingResultRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CognitiveTrainingResultService {

    @Autowired
    private CognitiveTrainingResultRepository cognitiveTrainingResultRepository;

    @Autowired
    private UserRepository userRepository;

    public CognitiveTrainingResult saveQuizResult(Long userId, int memoryScore, int languageScore, int spatialAbilityScore, int executiveFunctionScore, int attentionScore, int orientationScore) {
        int totalScore = memoryScore + languageScore + spatialAbilityScore + executiveFunctionScore + attentionScore + orientationScore;
        double percentile = calculatePercentile(totalScore);

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        int quizAttempt = getNextQuizAttempt(userId);

        CognitiveTrainingResult result = new CognitiveTrainingResult();
        result.setUserId(userId);
        result.setUsername(user.getUsername()); // 필요에 따라 사용
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

    public List<CognitiveTrainingResult> getQuizResultsByUserId(Long userId) {
        return cognitiveTrainingResultRepository.findByUserId(userId);
    }

    private int getNextQuizAttempt(Long userId) {
        List<CognitiveTrainingResult> userResults = cognitiveTrainingResultRepository.findByUserId(userId);
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


