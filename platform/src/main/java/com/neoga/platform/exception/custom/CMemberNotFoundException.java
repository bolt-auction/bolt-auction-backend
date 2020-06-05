package com.neoga.platform.exception.custom;

public class CMemberNotFoundException extends RuntimeException {
    public CMemberNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CMemberNotFoundException(String msg) {
        super(msg);
    }

    public CMemberNotFoundException() {
    }
}
