package org.samydevup.blogrestapi.service.impl;

import org.samydevup.blogrestapi.entity.Post;
import org.samydevup.blogrestapi.exception.ResourceNotFoundException;
import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.repository.PostRepository;
import org.samydevup.blogrestapi.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert postDto to post entity
        Post post = mapToEntity(postDto);
        //integration en base de donnees
        Post newPost = postRepository.save(post);
        //convert newPost to a PostDto for the PostDto method return object
        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> mapToDto(post)).toList();
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void DeletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",id));
        postRepository.delete(post);
    }

    /**
     * convertion d'entité en dto
     * @param post
     * @return
     */
    private PostDto mapToDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return  postDto;
    }

    /***
     * convertion de dto en entité
     * @param postDto
     * @return
     */
    private Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return  post;
    }
}
