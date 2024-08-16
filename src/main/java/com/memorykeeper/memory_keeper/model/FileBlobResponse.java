package com.memorykeeper.memory_keeper.model;

import java.util.Base64;

public class FileBlobResponse {
    private String originalFileName;
    private String storedFileName;
    private String fileData;  // Base64 encoded string

    public FileBlobResponse(String originalFileName, String storedFileName, byte[] fileData) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileData = Base64.getEncoder().encodeToString(fileData);  // Convert byte[] to Base64 string
    }

    // Getters and setters
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}


