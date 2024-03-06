package org.samydevup.blogrestapi.service;

import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePost(PostDto postDto,Long id);

    void DeletePostById(Long id);

    List<PostDto> getPostsByCategory(Long categoryId);
}
