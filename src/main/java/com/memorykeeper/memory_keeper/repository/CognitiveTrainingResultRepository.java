package com.memorykeeper.memory_keeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.memorykeeper.memory_keeper.model.CognitiveTrainingResult;

public interface CognitiveTrainingResultRepository extends JpaRepository<CognitiveTrainingResult, Long> {
    // Optional: 추가적인 쿼리 메서드 정의 가능
}
