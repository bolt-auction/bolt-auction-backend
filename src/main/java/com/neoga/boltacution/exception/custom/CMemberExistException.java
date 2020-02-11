package com.neoga.boltacution.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class CMemberExistException extends RuntimeException {
    public CMemberExistException(String msg, Throwable t) { super(msg, t); }
    public CMemberExistException(String msg) { super(msg); }
    public CMemberExistException() { }
}
