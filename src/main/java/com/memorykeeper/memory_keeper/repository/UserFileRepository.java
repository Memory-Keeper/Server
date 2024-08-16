package com.memorykeeper.memory_keeper.repository;

import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    Optional<UserFile> findByUserAndStoredFileName(User user, String storedFileName);
    List<UserFile> findByUser(User user);  // 특정 사용자의 모든 파일을 조회하는 메서드 추가
}



