package com.neoga.boltauction.exception.custom;


public class CExistEmailSignUpException extends RuntimeException{
    public CExistEmailSignUpException() { super(); }
    public CExistEmailSignUpException(String message) { super(message); }
}
