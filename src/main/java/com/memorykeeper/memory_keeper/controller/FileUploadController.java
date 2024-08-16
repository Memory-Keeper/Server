package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.repository.UserFileRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final String ALLOWED_EXTENSION = "jpg";
    private static final String ALLOWED_MIME_TYPE = "image/jpeg";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        User user = userOptional.get();

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds limit of 1MB");
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith("." + ALLOWED_EXTENSION)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file extension. Only JPG is allowed.");
        }

        // Validate MIME type
        String mimeType = file.getContentType();
        if (!ALLOWED_MIME_TYPE.equals(mimeType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only JPG is allowed.");
        }

        // Generate a random file name
        String randomFileName = UUID.randomUUID() + "." + ALLOWED_EXTENSION;

        // Ensure the directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        Path targetLocation = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Ensure the file has no execute permission
        File savedFile = targetLocation.toFile();
        savedFile.setExecutable(false);

        // Map the uploaded file to the user
        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setOriginalFileName(originalFilename);
        userFile.setStoredFileName(randomFileName);
        userFile.setFilePath(targetLocation.toString());

        userFileRepository.save(userFile);

        return ResponseEntity.ok("File uploaded successfully with name: " + randomFileName);
    }
    @GetMapping("/list")
    public ResponseEntity<List<String>> listUserFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = userOptional.get();

        // 사용자 파일 목록 조회
        List<UserFile> userFiles = userFileRepository.findByUser(user);
        List<String> filePaths = userFiles.stream()
                .map(UserFile::getFilePath)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filePaths);
    }
}



