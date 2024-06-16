package org.example.worker;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.queue.QueueClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.QueueMessage;
import org.example.entity.Comment;
import org.example.repository.CommentRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThumbnailImageWorker {
    private static final String BLOB_BASE_URL = "https://storageforfoodapp.blob.core.windows.net/food/";
    private final ThreadPoolTaskExecutor taskExecutor;
    private final QueueClient queueClient;
    private final BlobContainerClient foodBlobContainerClient;
    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper;

    public void run() {
        for (int i = 0; i < taskExecutor.getCorePoolSize(); i++) {
            taskExecutor.execute(() -> {
                log.info("Thread {} is listening to Azure Queue", Thread.currentThread().getName());
                listenAzureQueue(Thread.currentThread().getName());
            });
        }

    }
    private void listenAzureQueue(String threadName) {
        while (true) {
            queueClient.receiveMessages(1).forEach(message -> {
                try {
                    QueueMessage queueMessage = objectMapper.readValue(message.getBody().toBytes(), QueueMessage.class);
                    log.info("Thread {} is processing comment {}", threadName, queueMessage.getCommentId());
                    BlobClient blobClient = foodBlobContainerClient.getBlobClient(queueMessage.getCommentImgKey());
                    byte[] imageBytes = blobClient.downloadContent().toBytes();
                    byte[] resizedImage = resizeImage(imageBytes, 75, 75);
                    if(Objects.isNull(resizedImage)) {
                        log.error("Image format not recognized, cannot resize for commentId={}", queueMessage.getCommentId());
                        return;
                    }
                    String commentThumbnailKey = "comment-thumbnail-" + UUID.randomUUID() + ".bytes";
                    BlobClient thumbnailBlobClient = foodBlobContainerClient.getBlobClient(commentThumbnailKey);
                    thumbnailBlobClient.upload(BinaryData.fromBytes(resizedImage), true);

                    Comment comment = commentRepository.findById(queueMessage.getCommentId()).orElseThrow(() -> new RuntimeException("Comment not found"));
                    comment.setImageLinkThumbnail(BLOB_BASE_URL + commentThumbnailKey);
                    commentRepository.save(comment);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
                }
            });
        }
    }

    private byte[] resizeImage(byte[] imageBytes, int width, int height) throws IOException {
        // Convert byte array to BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(bis);
        bis.close();

        if(Objects.isNull(originalImage)) {
            return null;
        }
        else if (originalImage.getWidth() <= width && originalImage.getHeight() <= height) {
            return imageBytes;
        }

        // Resize the image
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

        // Convert BufferedImage to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedResizedImage, "jpg", bos);
        bos.close();

        return bos.toByteArray();
    }

}
