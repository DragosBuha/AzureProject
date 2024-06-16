package org.example.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlobService {
    private final BlobContainerClient foodBlobContainerClient;

    public void insertSomeBytesInBlobStorage(String someString, String blobName) {
        BlobClient blobClient = foodBlobContainerClient.getBlobClient(blobName);
        blobClient.upload(BinaryData.fromBytes(someString.getBytes()), true);
    }

    public byte[] getBlob(String blobName) {
        BlobClient blobClient = foodBlobContainerClient.getBlobClient(blobName);
        return blobClient.downloadContent().toBytes();
    }
}
