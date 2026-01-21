package com.dev.upload;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class FileServeController {

    private static final String UPLOAD_DIR = "uploads";

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String filename
    ) throws MalformedURLException {

        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
