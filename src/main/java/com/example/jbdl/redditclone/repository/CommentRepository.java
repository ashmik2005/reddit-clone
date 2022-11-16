package com.example.jbdl.redditclone.repository;

import com.example.jbdl.redditclone.model.Comment;
import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);

    List<Comment> findByPost(Post post);

}
