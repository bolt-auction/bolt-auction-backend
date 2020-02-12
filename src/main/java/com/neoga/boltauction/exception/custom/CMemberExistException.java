package com.neoga.boltauction.exception.custom;

public class CMemberExistException extends RuntimeException {
    public CMemberExistException(String msg, Throwable t) { super(msg, t); }
    public CMemberExistException(String msg) { super(msg); }
    public CMemberExistException() { }
}
