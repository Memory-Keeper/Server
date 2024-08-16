package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.repository.UserFileRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import com.memorykeeper.memory_keeper.model.FileBlobResponse;
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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Objects;

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
    public ResponseEntity<List<FileBlobResponse>> listUserFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }
        User user = userOptional.get();

        // 사용자 파일 목록 조회
        List<UserFile> userFiles = userFileRepository.findByUser(user);
        if (userFiles.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<FileBlobResponse> fileBlobResponses = userFiles.stream().map(userFile -> {
            Path filePath = Paths.get(userFile.getFilePath());
            byte[] fileData = null;
            try {
                if (Files.exists(filePath)) { // 파일이 존재하는지 확인
                    fileData = Files.readAllBytes(filePath);  // 파일 데이터를 바이트 배열로 읽기
                } else {
                    System.err.println("File not found: " + userFile.getFilePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fileData != null) {
                return new FileBlobResponse(userFile.getOriginalFileName(), userFile.getStoredFileName(), fileData);
            } else {
                return null; // 파일 데이터를 읽을 수 없는 경우 null 반환
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()); // null인 항목 제거

        return ResponseEntity.ok(fileBlobResponses);
    }
}




