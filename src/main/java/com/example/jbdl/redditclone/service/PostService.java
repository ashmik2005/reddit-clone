package com.example.jbdl.redditclone.service;

import com.example.jbdl.redditclone.dto.PostRequest;
import com.example.jbdl.redditclone.dto.PostResponse;
import com.example.jbdl.redditclone.exception.SpringRedditException;
import com.example.jbdl.redditclone.mapper.PostMapper;
import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.Subreddit;
import com.example.jbdl.redditclone.model.User;
import com.example.jbdl.redditclone.repository.PostRepository;
import com.example.jbdl.redditclone.repository.SubredditRepository;
import com.example.jbdl.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public Long createPost(PostRequest postRequest) {

        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException("Subreddit not found with name " + postRequest.getSubredditName()));
        User currentUser = authService.getCurrentUser();

        Post toSave = postMapper.mapToModel(postRequest, subreddit, currentUser);
        postRepository.save(toSave);
        return toSave.getPostId();
    }

    public PostResponse getPostById(Long id){
        Post post  = postRepository.findById(id).orElseThrow(() -> new SpringRedditException("Post not found with id " + id));
        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getAllPosts() {
        List<Post> posts =  postRepository.findAll();
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SpringRedditException("Subreddit not found with id " + subredditId));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());

    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with username " + username));

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }


}
