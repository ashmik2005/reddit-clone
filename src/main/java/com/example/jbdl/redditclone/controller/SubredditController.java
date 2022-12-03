package com.example.jbdl.redditclone.controller;

import com.example.jbdl.redditclone.dto.SubredditDto;
import com.example.jbdl.redditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        System.out.println("Request receieved by controller");
        return new ResponseEntity<SubredditDto>(subredditService.save(subredditDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits(){
        return new ResponseEntity<>(subredditService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(subredditService.getSubreddit(id), HttpStatus.OK);
    }



}
