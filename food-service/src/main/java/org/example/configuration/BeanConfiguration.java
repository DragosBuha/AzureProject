package org.example.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    private final String accountName = "storageforfoodapp";
    private final String accountKey = "J0UGnMg+hhUEYFmF6aDHo9eJ49WIV+IdjHSf33emRa/SHFwGuYbUgHn2mCxDW0RyVLP7TfPjbM8Q+AStISpFOw==";

    @Bean
    public QueueClient queueClient() {
        return new QueueClientBuilder()
                .endpoint(String.format("https://%s.queue.core.windows.net/", accountName))
                .queueName("test")
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }

    @Bean
    public BlobContainerClient foodBlobContainerClient() {
        return new BlobServiceClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net/", accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient()
                .getBlobContainerClient("food");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
