package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_pk", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer commentId;

    @Column(name = "text")
    private String text;

    @Column(name = "image_link_thumbnail")
    private String imageLinkThumbnail;

    @Column(name = "image_link_original")
    private String imageLinkOriginal;

    @Column(name = "recipe_pk")
    private Integer recipeId;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "user_pk")
    private Integer userId;
}
