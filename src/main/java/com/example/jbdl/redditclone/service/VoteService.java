package com.example.jbdl.redditclone.service;

import com.example.jbdl.redditclone.dto.VoteDto;
import com.example.jbdl.redditclone.exception.SpringRedditException;
import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.Vote;
import com.example.jbdl.redditclone.model.VoteType;
import com.example.jbdl.redditclone.repository.PostRepository;
import com.example.jbdl.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.jbdl.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    // TODO: Is it alright to access PostRepository in VoteService ?? (Could it run into cyclic dependencies ??)
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post not found with id: " + voteDto.getPostId()));

        // Now fetch the latest vote
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        // Not letting the user upvote or downvote more than once
        if (voteByPostAndUser.isPresent() &&
            voteByPostAndUser.get().getVoteType()
                    .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You cannot " + voteDto.getVoteType() + " a post more than once.");
        }

        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        // Saving changes to post entity in db
        voteRepository.save(mapToModel(voteDto, post));
        postRepository.save(post);

    }

    private Vote mapToModel(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

}
