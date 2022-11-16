package com.example.jbdl.redditclone.repository;

import com.example.jbdl.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Redundant
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
