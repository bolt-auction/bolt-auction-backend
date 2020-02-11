package com.neoga.boltauction.exception.advice;

import com.neoga.boltauction.exception.custom.CAuthEntryPointException;
import com.neoga.boltauction.exception.custom.CCommunicationException;
import com.neoga.boltauction.exception.custom.CEmailLoginFailedException;
import com.neoga.boltauction.exception.custom.CExistEmailSignUpException;
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

    @ExceptionHandler(CExistEmailSignUpException.class)
    public ResponseEntity CExistEmailSignUpException() { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }

    @ExceptionHandler(CCommunicationException.class)
    public ResponseEntity CCommunicationException() { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR ).build(); }
}
