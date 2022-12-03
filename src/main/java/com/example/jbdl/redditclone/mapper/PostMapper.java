package com.example.jbdl.redditclone.mapper;

import com.example.jbdl.redditclone.dto.PostRequest;
import com.example.jbdl.redditclone.dto.PostResponse;
import com.example.jbdl.redditclone.model.Post;
import com.example.jbdl.redditclone.model.Subreddit;
import com.example.jbdl.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target="createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target="subreddit", source = "subreddit")
    @Mapping(target="user", source = "user")
    @Mapping(target="description", source = "postRequest.description")
    Post mapToModel(PostRequest postRequest, Subreddit subreddit, User user);


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);


}
