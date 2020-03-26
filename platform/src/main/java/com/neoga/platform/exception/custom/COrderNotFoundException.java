package com.neoga.platform.exception.custom;

public class COrderNotFoundException extends RuntimeException {
    public COrderNotFoundException() {
        super();
    }

    public COrderNotFoundException(String message) {
        super(message);
    }
}
