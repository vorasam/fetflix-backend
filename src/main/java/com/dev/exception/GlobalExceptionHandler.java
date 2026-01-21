package com.dev.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest req){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),409,req.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),404,req.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();

        return new ResponseEntity<>(
                new ErrorResponse(msg,409,req.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 400, req.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
        return new ResponseEntity<>(
                new ErrorResponse("Internal server error", 500, req.getRequestURI()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
