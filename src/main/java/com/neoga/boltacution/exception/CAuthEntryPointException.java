package com.neoga.boltacution.exception;

public class CAuthEntryPointException extends RuntimeException {
    public CAuthEntryPointException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAuthEntryPointException(String msg) {
        super(msg);
    }

    public CAuthEntryPointException() {
        super();
    }
}
