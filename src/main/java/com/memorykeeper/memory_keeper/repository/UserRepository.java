package com.memorykeeper.memory_keeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.memorykeeper.memory_keeper.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
}