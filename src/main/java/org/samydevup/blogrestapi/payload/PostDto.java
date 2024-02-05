package org.samydevup.blogrestapi.payload;

import lombok.Data;
import org.samydevup.blogrestapi.entity.Comment;

import java.util.Set;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentDto> comments;
}
