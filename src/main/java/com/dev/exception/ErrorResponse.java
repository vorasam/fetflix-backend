package com.dev.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
    }
}
