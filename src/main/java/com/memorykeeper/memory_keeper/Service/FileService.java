package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.model.UserFile;
import com.memorykeeper.memory_keeper.repository.UserFileRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void saveFile(Long userId, MultipartFile file) throws IOException {
        // User 찾기
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        User user = userOptional.get();

        // 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + ".jpg";
        String filePath = uploadDir + "/" + storedFileName;

        // 파일 저장
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        // 파일 권한 설정
        File savedFile = new File(filePath);
        savedFile.setReadable(true, true);
        savedFile.setWritable(true, true);
        savedFile.setExecutable(false);

        // 매핑 정보 저장
        UserFile userFile = new UserFile();
        userFile.setUser(user);
        userFile.setOriginalFileName(originalFileName);
        userFile.setStoredFileName(storedFileName);
        userFile.setFilePath(filePath);

        userFileRepository.save(userFile);
    }

    public Optional<UserFile> getFile(Long userId, String originalFileName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return userFileRepository.findByUserAndOriginalFileName(userOptional.get(), originalFileName);
    }
}

