package com.example.jbdl.redditclone.repository;

import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.Subreddit;
import com.example.jbdl.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

}
