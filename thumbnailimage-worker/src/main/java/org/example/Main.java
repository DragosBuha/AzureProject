package org.example;

import org.example.worker.ThumbnailImageWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ThumbnailImageWorker thumbnailImageWorker = context.getBean(ThumbnailImageWorker.class);
        thumbnailImageWorker.run();
    }
}