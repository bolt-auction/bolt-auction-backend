package com.neoga.platform.exception.custom;

public class CAlreadySignUpException extends RuntimeException {
    public CAlreadySignUpException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAlreadySignUpException(String msg) {
        super(msg);
    }

    public CAlreadySignUpException() {
    }
}
