package org.samydevup.blogrestapi.service.impl;

import org.modelmapper.ModelMapper;
import org.samydevup.blogrestapi.entity.Comment;
import org.samydevup.blogrestapi.entity.Post;
import org.samydevup.blogrestapi.exception.BlogAPIException;
import org.samydevup.blogrestapi.exception.ResourceNotFoundException;
import org.samydevup.blogrestapi.payload.CommentDto;
import org.samydevup.blogrestapi.repository.CommentRepository;
import org.samydevup.blogrestapi.repository.PostRepository;
import org.samydevup.blogrestapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        /**
         * 1-convertir l'objet dto en entité ,
         * 2-recuperé le post pour lequel on voudrais créer un commentaire pour le setté au nouveau commentaire
         * 3-et finir par intégrer le nouveau commentaire en base .
         */
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long id) {
        List<Comment> listOfComment = commentRepository.findByPostId(id);
        return listOfComment.stream().map(c -> mapToDTO(c)).toList();
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        //verif des recups
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "ce commentaire n'appartient pas au post !");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Comment comment = new Comment();
        try {
            comment = commentPostLinkCheck(postId, commentId);
            comment.setName(commentRequest.getName());
            comment.setEmail(commentRequest.getEmail());
            comment.setBody(commentRequest.getBody());
            comment = commentRepository.save(comment);
        } catch (BlogAPIException exception) {
            exception.printStackTrace();
        }
        return mapToDTO(comment);

    }

    @Override
    public void deleteComment(long postId, long commentId) {
        try {
            Comment comment = commentPostLinkCheck(postId, commentId);
            if (!Objects.isNull(comment)) {
                commentRepository.delete(comment);
            }
        }catch (BlogAPIException b){
            b.printStackTrace();
        }
    }


    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment,CommentDto.class);
        /*
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
         */
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto,Comment.class);
        /*
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
         */
        return comment;
    }

    private Comment commentPostLinkCheck(long postId, long commentId) throws BlogAPIException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "ce commentaire n'appartient pas au post !");
        } else {
            return comment;
        }
    }

}
