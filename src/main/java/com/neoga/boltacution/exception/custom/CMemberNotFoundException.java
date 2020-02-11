package com.neoga.boltacution.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class CMemberNotFoundException extends RuntimeException {
    public CMemberNotFoundException(String msg, Throwable t) { super(msg, t); }
    public CMemberNotFoundException(String msg) { super(msg); }
    public CMemberNotFoundException() { }
}
