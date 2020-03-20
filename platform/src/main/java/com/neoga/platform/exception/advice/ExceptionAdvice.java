package com.neoga.platform.exception.advice;

import com.neoga.platform.exception.custom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(CAuthEntryPointException.class)
    public ResponseEntity authEntryPointException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CEmailLoginFailedException.class)
    public ResponseEntity emailLoginFailedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity AccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CExistUidSignUpException.class)
    public ResponseEntity CExistUidSignUpException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CAlreadySignUpException.class)
    public ResponseEntity CMemberExistException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CMemberNotFoundException.class)
    public ResponseEntity CMemberNotFoundException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CCommunicationException.class)
    public ResponseEntity CCommunicationException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
