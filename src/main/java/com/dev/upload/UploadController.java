package com.dev.upload;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class UploadController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // ✅ Create upload directory if missing
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ✅ Clean filename + prevent overwrite
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "_" + originalFilename;

        Path filePath = uploadPath.resolve(filename);

        // ✅ Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ✅ Return only filename
        return ResponseEntity.ok(filename);
    }
}