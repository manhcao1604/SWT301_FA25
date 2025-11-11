package com.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Lưu file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("File stored successfully: {}", filePath.toString());
        return "/uploads/" + subDirectory + "/" + fileName;
    }

    public List<String> storeFiles(List<MultipartFile> files, String subDirectory) throws IOException {
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = storeFile(file, subDirectory);
                filePaths.add(filePath);
            }
        }

        return filePaths;
    }

    public boolean deleteFile(String filePath) {
        try {
            // Remove leading slash if present
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }

            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            return false;
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            log.info("Upload directory created: {}", uploadDir);
        } catch (IOException e) {
            log.error("Could not create upload directory: {}", e.getMessage());
        }
    }
}