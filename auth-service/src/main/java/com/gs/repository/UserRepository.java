package com.gs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}