package org.example.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCommentDTO {
    private Integer commentId;
    private String text;
    private String thumbnailImageLink;
    private String originalImageLink;
    private Integer rating;
    private Integer userId;
    private String userName;
}
