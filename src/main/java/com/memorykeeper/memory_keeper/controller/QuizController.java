package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.Service.QuizGenerationService;
import com.memorykeeper.memory_keeper.model.QuizQuestion;
import com.memorykeeper.memory_keeper.model.QuizResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizController {

    @Autowired
    private QuizGenerationService quizGenerationService;

    @GetMapping("/generate-cognitive-quiz-from-json")
    public List<QuizQuestion> generateCognitiveQuizFromJson(@RequestParam String jsonFilePath) {
        return quizGenerationService.generateCognitiveQuizFromJsonFile(jsonFilePath);
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam int questionIndex, @RequestParam int selectedAnswer, @RequestBody List<QuizQuestion> quizQuestions) {
        // 인덱스 유효성 검사
        if (questionIndex < 0 || questionIndex >= quizQuestions.size()) {
            return "유효하지 않은 질문 인덱스입니다.";
        }

        QuizQuestion question = quizQuestions.get(questionIndex);
        boolean isCorrect = question.getCorrectAnswer() == selectedAnswer;
        quizGenerationService.storeQuizResult(question.getCognitiveFunction(), isCorrect);

        return isCorrect ? "정답입니다!" : "오답입니다. 정답은 " + question.getCorrectAnswer() + "번 입니다.";
    }

    @GetMapping("/quiz-results")
    public List<QuizResult> getQuizResults() {
        return quizGenerationService.getQuizResults();
    }
}

