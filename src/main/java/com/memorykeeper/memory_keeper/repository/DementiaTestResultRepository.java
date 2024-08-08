package com.memorykeeper.memory_keeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.memorykeeper.memory_keeper.model.DementiaTestResult;

public interface DementiaTestResultRepository extends JpaRepository<DementiaTestResult, Long> {
}