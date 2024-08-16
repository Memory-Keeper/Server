package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    Optional<UserFile> findByUserAndOriginalFileName(User user, String originalFileName);
}

