package com.java.project.S3FileManager;

import com.java.project.S3FileManager.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class S3FileManagerApplication {

	private final Logger log = LoggerFactory.getLogger(S3FileManagerApplication.class);

	@Autowired
	S3Service s3Service;

	@Value("${aws.s3.bucket-name}")
	private String bucketName; // Injecting bucket name from properties

	public static void main(String[] args) {
		SpringApplication.run(S3FileManagerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initializeBucket() {
		if(!s3Service.doesBucketExist(bucketName)) {
			s3Service.createBucket(bucketName);
		} else {
			log.info("Bucket already exists");
		}
	}

}
