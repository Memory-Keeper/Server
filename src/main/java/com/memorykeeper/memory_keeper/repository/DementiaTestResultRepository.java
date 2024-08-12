package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.DementiaTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DementiaTestResultRepository extends JpaRepository<DementiaTestResult, Long> {
    List<DementiaTestResult> findByUsername(String username);
}