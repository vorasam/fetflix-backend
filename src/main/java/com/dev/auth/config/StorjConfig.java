package com.dev.auth.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorjConfig {

    @Value("${storj.access-key}")
    private String accessKey;

    @Value("${storj.secret-key}")
    private String secretKey;

    @Value("https://gateway.storjshare.io")
    private String endpoint;

    @Bean
    public AmazonS3 storjClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpoint,
                                "us-east-1" // Storj doesn't use regions, but AWS SDK requires this
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true) // Important for Storj
                .build();
    }
}