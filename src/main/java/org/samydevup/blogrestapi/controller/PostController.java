package org.samydevup.blogrestapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.payload.PostDtoV2;
import org.samydevup.blogrestapi.payload.PostResponse;
import org.samydevup.blogrestapi.service.PostService;
import org.samydevup.blogrestapi.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
@Tag(//annotation swagger permettant customiser le swagger-ui avec des infos en plus
        name = "CRUD REST APIs for Post Resource"
)
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    /**
     * @param postDto
     * @return
     * @PreAutorize("hasRole('ADMIN')") Annotation ne permettant qu'au user
     * springSecurity de role ADMIN de créer un post
     */
    @SecurityRequirement(name = "Bear Authentication")
    //se materialise par l'ajout du cadenas sur le endpoint dans swagger-ui
    @PreAuthorize("hasRole('ADMIN')")
    //annotations : @Opeations et @ApiResponse utilisées pour ajouter des métadonnées au endpoints
    @Operation(
            summary = "Create Post REST API",
            description = "Create POST REST API is used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        /**
         PostDto postDto1 = postService.createPost(postDto);
         return  new ResponseEntity<>(postDto1, HttpStatus.CREATED);
         */
        //oubien simplement
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Posts REST API", description = "Get all Posts REST API is used to fetch all the posts from the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(summary = "Get Post by id REST API", description = "Get Post By Id REST API is used to get single post from the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping(value = "/api/posts/{id}", params = "version=1")
//a test via : http://localhost:8080/api/posts/idPost?version=1
    public ResponseEntity<PostDto> getPostByIdV1(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "Get Post by id REST API", description = "Get Post with tag's informations , by id rest api is used to get single post from the database")
    @ApiResponse(responseCode = "200", description = "Http status 200 success")
    @GetMapping(value = "/api/posts/{id}", params = "version=2")
//a tester via : http://localhost:8080/api/post/idPost?version=2
    public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable("id") Long id) {
        PostDto postDto = postService.getPostById(id);
        PostDtoV2 postDtoV2 = new PostDtoV2();
        postDtoV2.setId(postDto.getId());
        postDtoV2.setTitle(postDto.getTitle());
        postDtoV2.setDescription(postDto.getDescription());
        postDtoV2.setContent(postDto.getContent());
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring Boot");
        tags.add("AWS");
        postDtoV2.setTags(tags);
        return ResponseEntity.ok(postDtoV2);
    }

    @Operation(summary = "Update Post REST API", description = "Update Post REST API is used to update a particular post in the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id") Long id) {
        PostDto updatedPostDtoResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(updatedPostDtoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete Post REST API", description = "Delete Post REST API is used to delete a particular post in the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        postService.DeletePostById(id);
        return new ResponseEntity<>("Post deleted successfuly", HttpStatus.OK);
    }

    @GetMapping("/api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId) {
        return new ResponseEntity<>(postService.getPostsByCategory(categoryId), HttpStatus.OK);
    }


}
