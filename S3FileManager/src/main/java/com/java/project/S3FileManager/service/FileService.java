package com.java.project.S3FileManager.service;

import com.java.project.S3FileManager.model.FileMetadata;
import com.java.project.S3FileManager.repo.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {


    @Autowired
    private S3Service s3Service;

    private final FileMetadataRepository repo;

    @Value("${aws.s3.bucket-name}")
    private String bucketName; // Injecting bucket name from properties

    public FileService(FileMetadataRepository repo) {
        this.repo = repo;
    }


    public FileMetadata saveFile(MultipartFile file, FileMetadata metadata) {
        try {
            //Convert multipart file to File (temporary)
            File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(convertedFile);

            //upload the file to AWS S3
            String keyName = "folder/" + file.getOriginalFilename(); //S3 key
            String s3Uri = s3Service.uploadFile(bucketName, keyName, convertedFile);

            // Add S3 URI to metadata
            metadata.setUrl(s3Uri);

            return  repo.save(metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileMetadata> findAll() {
        return repo.findAll();
    }

    public Optional<FileMetadata> findById(String id) {
        return repo.findById(id);
    }

    public void DeleteFileById(String id) {
      Optional<FileMetadata> metadata = repo.findById(id);
      if(metadata.isPresent()){
          String uri = metadata.get().getUrl();
          s3Service.deleteFile(uri);
          repo.deleteById(id);
      }

    }
}
