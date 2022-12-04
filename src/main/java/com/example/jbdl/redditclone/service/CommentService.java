package com.example.jbdl.redditclone.service;


import com.example.jbdl.redditclone.dto.CommentsDto;
import com.example.jbdl.redditclone.exception.SpringRedditException;
import com.example.jbdl.redditclone.mapper.CommentMapper;
import com.example.jbdl.redditclone.model.Comment;
import com.example.jbdl.redditclone.model.NotificationEmail;
import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.User;
import com.example.jbdl.redditclone.repository.CommentRepository;
import com.example.jbdl.redditclone.repository.PostRepository;
import com.example.jbdl.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder contentBuilder;
    private final MailService mailService;
    private final UserRepository userRepository;


    public void save(CommentsDto commentsDto) {
        System.out.println("postId = " + commentsDto.getPostId());
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post not found with id " + commentsDto.getPostId()));

        // Getting the current logged-in user (From the authentication context)
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.mapToModel(commentsDto, post, user);
        commentRepository.save(comment);

        // Send a notification email to the creator of the post about the new added comment
        String message = contentBuilder.build(comment.getUser().getUsername() + "posted a comment on your post. ");
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("Post not found with id: " + postId));
        return commentRepository.findByPost(post).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with username: " + username));

        return commentRepository.findAllByUser(user).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());

    }



}
