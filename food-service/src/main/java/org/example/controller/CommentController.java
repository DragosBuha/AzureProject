package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.dto.CommentDTO;
import org.example.entity.dto.MenuCommentDTO;
import org.example.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<MenuCommentDTO> getAllCommentsForRecipe(@RequestParam("recipeId") Integer recipeId) {
        return commentService.getCommentsForRecipe(recipeId);
    }

    @PostMapping
    public void addComment(@RequestHeader("USER_ID") Integer userId, @ModelAttribute CommentDTO commentDTO) throws IOException {
        commentService.addComment(userId, commentDTO);
    }
}
