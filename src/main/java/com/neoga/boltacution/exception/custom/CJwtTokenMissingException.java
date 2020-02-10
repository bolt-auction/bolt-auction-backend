package com.neoga.boltacution.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class CJwTokenMissingException extends AuthenticationException {
    public CJwTokenMissingException(String msg, Throwable t) {
        super(msg, t);
    }

    public CJwTokenMissingException(String msg) {
        super(msg);
    }

}
