package com.neoga.boltauction.exception.custom;

public class CBidExistException extends RuntimeException {
    public CBidExistException(String message) {
        super(message);
    }
}
