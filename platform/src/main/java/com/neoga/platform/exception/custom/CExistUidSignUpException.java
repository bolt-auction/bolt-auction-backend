package com.neoga.platform.exception.custom;


public class CExistUidSignUpException extends RuntimeException {
    public CExistUidSignUpException() {
        super();
    }

    public CExistUidSignUpException(String message) {
        super(message);
    }
}
