package com.neoga.boltacution.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class CJwtTokenMissingException extends AuthenticationException {
    public CJwtTokenMissingException(String msg, Throwable t) {
        super(msg, t);
    }

    public CJwtTokenMissingException(String msg) {
        super(msg);
    }

}
