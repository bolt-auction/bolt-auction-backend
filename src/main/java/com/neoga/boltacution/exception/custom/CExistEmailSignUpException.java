package com.neoga.boltacution.exception.custom;


public class CExistEmailSignUpException extends RuntimeException{
    public CExistEmailSignUpException() { super(); }
    public CExistEmailSignUpException(String message) { super(message); }
}
