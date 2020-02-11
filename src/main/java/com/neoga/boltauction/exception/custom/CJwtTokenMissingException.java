package com.neoga.boltauction.exception.custom;

public class CJwtTokenMissingException extends RuntimeException {
    public CJwtTokenMissingException(String msg, Throwable t) {
        super(msg, t);
    }
    public CJwtTokenMissingException(String msg) {
        super(msg);
    }
    public CJwtTokenMissingException(){ }

}
