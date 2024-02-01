package org.samydevup.blogrestapi.service;

import org.samydevup.blogrestapi.payload.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();

    PostDto getPostById(Long id);

    PostDto updatePost(PostDto postDto,Long id);

    void DeletePostById(Long id);
}
