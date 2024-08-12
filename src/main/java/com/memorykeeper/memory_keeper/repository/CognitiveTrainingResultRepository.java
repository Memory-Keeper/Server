package com.memorykeeper.memory_keeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;
import java.util.List;

public interface CognitiveTrainingResultRepository extends JpaRepository<CognitiveTrainingResult, Long> {
    List<CognitiveTrainingResult> findByUsername(String username);
}

