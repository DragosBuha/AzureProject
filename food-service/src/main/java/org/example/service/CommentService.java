package org.example.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.queue.QueueClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Comment;
import org.example.entity.dto.CommentDTO;
import org.example.entity.dto.MenuCommentDTO;
import org.example.repository.CommentRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private static final String BLOB_BASE_URL = "https://storageforfoodapp.blob.core.windows.net/food/";
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlobContainerClient foodBlobContainerClient;
    private final QueueClient queueClient;
    private final ObjectMapper objectMapper;

    public List<MenuCommentDTO> getCommentsForRecipe(Integer recipeId) {
        return commentRepository.findAllByRecipeId(recipeId).stream()
                .map(comment -> MenuCommentDTO.builder()
                        .commentId(comment.getCommentId())
                        .text(comment.getText())
                        .thumbnailImageLink(comment.getImageLinkThumbnail())
                        .rating(comment.getRating())
                        .userId(comment.getUserId())
                        .userName(userRepository.findById(comment.getUserId()).get().getFullName())
                        .build()
                )
                .toList();
    }

    @Transactional
    public void addComment(Integer userId, CommentDTO commentDTO) throws IOException {
        String commentImgKey = "comment-img-" + UUID.randomUUID() + ".bytes";
        BlobClient blobClient = foodBlobContainerClient.getBlobClient(commentImgKey);
        blobClient.upload(BinaryData.fromBytes(commentDTO.getFile().getBytes()), true);

        String imageLinkOriginal = BLOB_BASE_URL + commentImgKey;
        Comment comment = commentRepository.save(Comment.builder()
                .text(commentDTO.getText())
                .imageLinkOriginal(imageLinkOriginal)
                .recipeId(commentDTO.getRecipeId())
                .rating(commentDTO.getRating())
                .userId(userId)
                .build()
        );

        try {
            queueClient.sendMessage(objectMapper.writeValueAsString(Map.of(
                    "commentId", comment.getCommentId(),
                    "commentImgKey", commentImgKey
            )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
