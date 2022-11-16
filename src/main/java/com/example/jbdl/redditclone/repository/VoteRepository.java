package com.example.jbdl.redditclone.repository;

import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.User;
import com.example.jbdl.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
