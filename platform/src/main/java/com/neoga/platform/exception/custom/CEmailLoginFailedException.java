package com.neoga.platform.exception.custom;

public class CEmailLoginFailedException extends RuntimeException {
    public CEmailLoginFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmailLoginFailedException(String msg) {
        super(msg);
    }

    public CEmailLoginFailedException() {
        super();
    }
}
