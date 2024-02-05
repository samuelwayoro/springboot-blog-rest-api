package org.samydevup.blogrestapi.service;

import org.samydevup.blogrestapi.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId,CommentDto commentDto);
    List<CommentDto> getCommentsByPostId(Long id);

    CommentDto getCommentById(Long postId,Long commentId);

    CommentDto updateComment(long postId,long commentId , CommentDto commentRequest);

    void deleteComment(long postId,long commentId);
}
