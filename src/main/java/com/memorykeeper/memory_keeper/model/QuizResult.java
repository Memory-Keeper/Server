package com.memorykeeper.memory_keeper.model;

public class QuizResult {
    private String cognitiveFunction; // 인지 기능 영역
    private int totalQuestions;
    private int correctAnswers;

    // Constructor
    public QuizResult(String cognitiveFunction, int totalQuestions, int correctAnswers) {
        this.cognitiveFunction = cognitiveFunction;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }

    // Getters and Setters
    public String getCognitiveFunction() {
        return cognitiveFunction;
    }

    public void setCognitiveFunction(String cognitiveFunction) {
        this.cognitiveFunction = cognitiveFunction;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
