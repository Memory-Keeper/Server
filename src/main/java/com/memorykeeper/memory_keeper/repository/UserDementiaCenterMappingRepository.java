package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.UserDementiaCenterMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDementiaCenterMappingRepository extends JpaRepository<UserDementiaCenterMapping, Long> {
    boolean existsByUserIdAndDementiaCenterId(Long userId, Long dementiaCenterId);
}

