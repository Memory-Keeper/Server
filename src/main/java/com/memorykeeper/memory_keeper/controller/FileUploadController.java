package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.repository.UserFileRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB
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
        String randomFileName = UUID.randomUUID().toString() + "." + ALLOWED_EXTENSION;

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

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        try {
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

            Optional<UserFile> userFileOptional = userFileRepository.findByUserAndOriginalFileName(user, fileName);
            if (userFileOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            UserFile userFile = userFileOptional.get();
            Path filePath = Paths.get(userFile.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

