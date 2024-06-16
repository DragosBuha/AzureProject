package org.example.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommentDTO {
    private String text;
    private Integer recipeId;
    private Integer rating;
    MultipartFile file;
}
