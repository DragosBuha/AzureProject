package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.BlobService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blobs")
public class BlobsController {
    private final BlobService blobService;

    @PostMapping("/{blobName}")
    public void insertSomeBytesInBlobStorage(@RequestBody String someString, @PathVariable("blobName") String blobName) {
        blobService.insertSomeBytesInBlobStorage(someString, blobName);
    }

    @GetMapping("/{blobName}")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getBlob(@PathVariable("blobName") String blobName) {
        return blobService.getBlob(blobName);
    }
}
