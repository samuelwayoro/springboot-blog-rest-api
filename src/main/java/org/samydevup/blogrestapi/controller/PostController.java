package org.samydevup.blogrestapi.controller;

import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create a blog
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        /**
         PostDto postDto1 = postService.createPost(postDto);
         return  new ResponseEntity<>(postDto1, HttpStatus.CREATED);
         */
        //oubien simplement
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post
    @GetMapping
    public List<PostDto> getAllPosts(){
        return  postService.getAllPosts();
    }

    //get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id ){
       return ResponseEntity.ok(postService.getPostById(id));
    }

    //update a post by id
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable("id") Long id){
        PostDto updatedPostDtoResponse = postService.updatePost(postDto,id);
        return  new ResponseEntity<>(updatedPostDtoResponse,HttpStatus.OK);
    }

    //delete a post by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
        postService.DeletePostById(id);
        return new ResponseEntity<>("Post deleted successfuly",HttpStatus.OK);
    }

}
