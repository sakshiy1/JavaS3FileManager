package com.java.project.S3FileManager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.net.URI;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final  Logger log = LoggerFactory.getLogger(S3Service.class);


    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    public boolean doesBucketExist(String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    public void createBucket(String bucketName){
        try {
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(request);
            log.debug("Bucket created");

        } catch(S3Exception e) {
            throw new RuntimeException("Unable to create bucket");
        }
    }

    public String uploadFile(String bucketName, String keyName, File file) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(request, file.toPath());
            return String.format("https://%s.s3.amazonaws.com/%s",bucketName,keyName);
        } catch (S3Exception e){
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }

    public String deleteFile(String uri) {
        try {
            URI s3uri = URI.create(uri);
            log.info("hello uri %s", s3uri);
            String bucketName = s3uri.getHost().split("\\.")[0];
            log.info("hello bucketnmame %s", bucketName);
            String keyName = s3uri.getPath().substring(1);
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.deleteObject(request);
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, keyName);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing URI");
        }
    }


}
