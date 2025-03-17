package com.java.project.S3FileManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.S3FileManager.model.FileMetadata;
import com.java.project.S3FileManager.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/files")
public class FileController {

    private final FileService fileservice;

    public FileController(FileService service, FileService fileservice) {
        this.fileservice = fileservice;
    }

    @PostMapping(value= "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileMetadata> saveFile(@RequestParam("file") MultipartFile file, @RequestParam String fileMetadataJson) throws IOException {

        //convert json string to java object
        ObjectMapper objectMapper = new ObjectMapper();
        FileMetadata fileMetadata = objectMapper.readValue(fileMetadataJson,FileMetadata.class);
        return ResponseEntity.ok(fileservice.saveFile(file,fileMetadata));
    }

    @GetMapping("/get")
    public ResponseEntity<List<FileMetadata>> getFile(){
        return ResponseEntity.ok(fileservice.findAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<FileMetadata>> getById(@PathVariable String id){
       if(fileservice.findById(id).isPresent()){
        return ResponseEntity.ok(fileservice.findById(id));
    }
        return null;
    }

    @DeleteMapping("/remove/{id}")
    public void deleteById(@PathVariable String id) {
        fileservice.DeleteFileById(id);
    }


}
