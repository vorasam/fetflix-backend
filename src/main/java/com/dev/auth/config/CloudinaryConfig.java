package com.dev.auth.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dvcw0oqca");
        config.put("api_key", "112263926558927");
        config.put("api_secret", "qZVN043Dt0FBb8RIDFtKF8o-pao");
        return new Cloudinary(config);
    }
}
