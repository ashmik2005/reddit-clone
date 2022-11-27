package com.example.jbdl.redditclone.repository;

import com.example.jbdl.redditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subRedditName);

    Optional<Subreddit> findById(Long id);
}
