package com.memorykeeper.memory_keeper.model;

import java.util.List;

public class QuizQuestion {
    private String questionText;
    private List<String> options;
    private int correctAnswer; // 1-based index
    private String imageUrl;
    private String cognitiveFunction; // 인지 기능 영역: 예) 언어능력, 시공간 능력 등

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCognitiveFunction() {
        return cognitiveFunction;
    }

    public void setCognitiveFunction(String cognitiveFunction) {
        this.cognitiveFunction = cognitiveFunction;
    }
}
