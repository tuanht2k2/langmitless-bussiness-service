package com.kma.engfinity.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
  private final String uploadDir = "uploads/audio";
  public String storeFile(MultipartFile file) {
    try {

      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Path targetLocation = uploadPath.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return targetLocation.toString();
    } catch (IOException e) {
      throw new RuntimeException("Lưu file thất bại!", e);
    }
  }
}
