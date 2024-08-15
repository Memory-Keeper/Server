package com.memorykeeper.memory_keeper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class DementiaTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 사용자 ID 추가
    private String username;
    private int score;
    private boolean isDementiaSuspected;
    private int testAttempt; // 시행 회차
    private LocalDate testDate; // 시행 날짜

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isDementiaSuspected() {
        return isDementiaSuspected;
    }

    public void setDementiaSuspected(boolean dementiaSuspected) {
        isDementiaSuspected = dementiaSuspected;
    }

    public int getTestAttempt() {
        return testAttempt;
    }

    public void setTestAttempt(int testAttempt) {
        this.testAttempt = testAttempt;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }
}

