package com.java.project.S3FileManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class FileMetadata {

    @Id
    private String id;
    private String filename;
    private String url;
    private long size;
    private String uploadData;

    public FileMetadata() {
    }

    public FileMetadata(String id, String uploadData, String url, String filename, long size) {
        this.id = id;
        this.uploadData = uploadData;
        this.url = url;
        this.filename = filename;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUploadData() {
        return uploadData;
    }

    public void setUploadData(String uploadData) {
        this.uploadData = uploadData;
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                ", uploadData='" + uploadData + '\'' +
                '}';
    }
}
