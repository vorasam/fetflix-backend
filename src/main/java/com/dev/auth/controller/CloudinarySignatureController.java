package com.dev.auth.controller;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class CloudinarySignatureController {

    private final Cloudinary cloudinary;

    @GetMapping("/signature")
    public Map<String, Object> getSignature() {

        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", timestamp);
        params.put("folder", "fetflix/videos");

        String signature = cloudinary.apiSignRequest(
                params,
                cloudinary.config.apiSecret
        );

        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("cloudName", cloudinary.config.cloudName);
        response.put("apiKey", cloudinary.config.apiKey);

        return response;
    }
}
