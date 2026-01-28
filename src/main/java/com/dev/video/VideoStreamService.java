package com.dev.video;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class VideoStreamService {

    private final AmazonS3 storjClient;

    @Value("fetflix-video")
    private String bucket;

    public String getSignedUrl(String videoKey) {

        Date expiry = new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000);


        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, videoKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiry);

        return storjClient.generatePresignedUrl(request).toString();
    }
}
