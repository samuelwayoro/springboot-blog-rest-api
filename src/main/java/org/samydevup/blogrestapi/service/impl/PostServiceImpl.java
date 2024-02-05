package org.samydevup.blogrestapi.service.impl;

import org.modelmapper.ModelMapper;
import org.samydevup.blogrestapi.entity.Post;
import org.samydevup.blogrestapi.exception.ResourceNotFoundException;
import org.samydevup.blogrestapi.payload.PostDto;
import org.samydevup.blogrestapi.payload.PostResponse;
import org.samydevup.blogrestapi.repository.PostRepository;
import org.samydevup.blogrestapi.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        //s'assurer d'avoir une bonne valeur de sortDir comprise entre ASC ou DSC
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //implémentation de la pagination :
        //construre un objet de ty Pageable à partir des paramètres pageNo et pageSize et sortBy
        //Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy)); //possible d'ordonner asc ou desc avec Sort.by(sortBy).Asc

        //implémentation optimisée
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        //renvoiyer un Page<T> a partir de ce pageable à partir de la méthode findAll de Jpa
        Page<Post> posts = postRepository.findAll(pageable);

        //recuperer le contenu du Page<T> (.getContent()) qui est un List<T>
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).toList();
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void DeletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        postRepository.delete(post);
    }

    /**
     * convertion d'entité en dto
     *
     * @param post
     * @return
     */
    private PostDto mapToDto(Post post) {
        PostDto postDto = modelMapper.map(post,PostDto.class);
        /*
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        */
        return postDto;
    }

    /***
     * convertion de dto en entité
     * @param postDto
     * @return
     */
    private Post mapToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto,Post.class);
        /*
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
         */
        return post;
    }
}
