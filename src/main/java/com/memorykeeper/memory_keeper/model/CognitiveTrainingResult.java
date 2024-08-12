package com.memorykeeper.memory_keeper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class CognitiveTrainingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int memoryScore;
    private int languageScore;
    private int spatialAbilityScore;
    private int executiveFunctionScore;
    private int attentionScore;
    private int orientationScore;
    private int totalScore;
    private double percentile;
    private int quizAttempt;
    private LocalDate quizDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMemoryScore() {
        return memoryScore;
    }

    public void setMemoryScore(int memoryScore) {
        this.memoryScore = memoryScore;
    }

    public int getLanguageScore() {
        return languageScore;
    }

    public void setLanguageScore(int languageScore) {
        this.languageScore = languageScore;
    }

    public int getSpatialAbilityScore() {
        return spatialAbilityScore;
    }

    public void setSpatialAbilityScore(int spatialAbilityScore) {
        this.spatialAbilityScore = spatialAbilityScore;
    }

    public int getExecutiveFunctionScore() {
        return executiveFunctionScore;
    }

    public void setExecutiveFunctionScore(int executiveFunctionScore) {
        this.executiveFunctionScore = executiveFunctionScore;
    }

    public int getAttentionScore() {
        return attentionScore;
    }

    public void setAttentionScore(int attentionScore) {
        this.attentionScore = attentionScore;
    }

    public int getOrientationScore() {
        return orientationScore;
    }

    public void setOrientationScore(int orientationScore) {
        this.orientationScore = orientationScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public int getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(int quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public LocalDate getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(LocalDate quizDate) {
        this.quizDate = quizDate;
    }
}

