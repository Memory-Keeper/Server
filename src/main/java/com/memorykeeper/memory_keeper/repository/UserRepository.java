package com.memorykeeper.memory_keeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.memorykeeper.memory_keeper.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}