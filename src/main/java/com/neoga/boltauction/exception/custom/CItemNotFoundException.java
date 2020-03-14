package com.neoga.boltauction.exception.custom;

public class CItemNotFoundException extends RuntimeException {

    public CItemNotFoundException(String message) {
        super(message);
    }
}
