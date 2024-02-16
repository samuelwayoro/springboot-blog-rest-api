package org.samydevup.blogrestapi.controller;

import jakarta.validation.Valid;
import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.payload.PostResponse;
import org.samydevup.blogrestapi.service.PostService;
import org.samydevup.blogrestapi.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create a blog post
    //annotation ne permettant qu'au user springSecurity de type l'Admin de créer un post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        /**
         PostDto postDto1 = postService.createPost(postDto);
         return  new ResponseEntity<>(postDto1, HttpStatus.CREATED);
         */
        //oubien simplement
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post
    @GetMapping
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //update a post by id
    //annotation ne permettant qu'au user springSecurity de type l'Admin de update un post
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id") Long id) {
        PostDto updatedPostDtoResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(updatedPostDtoResponse, HttpStatus.OK);
    }

    //delete a post by id
    //annotation ne permettant qu'au user springSecurity de type l'Admin de delete un post
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        postService.DeletePostById(id);
        return new ResponseEntity<>("Post deleted successfuly", HttpStatus.OK);
    }


}
