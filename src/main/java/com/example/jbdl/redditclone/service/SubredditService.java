package com.example.jbdl.redditclone.service;

import com.example.jbdl.redditclone.dto.SubredditDto;
import com.example.jbdl.redditclone.exception.SpringRedditException;
import com.example.jbdl.redditclone.mapper.SubredditMapper;
import com.example.jbdl.redditclone.model.Subreddit;
import com.example.jbdl.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapToModel(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(
                () -> new SpringRedditException("Subreddit not found with id = " + id)
        );

        return subredditMapper.mapToDto(subreddit);

    }


//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//        return Subreddit.builder()
//                .name(subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .build();
//    }
//
//    // Use mapStruct or modelMapper for this
//    private SubredditDto mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder()
//                .name(subreddit.getName())
//                .id(subreddit.getId())
//                .description(subreddit.getDescription())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }


}
