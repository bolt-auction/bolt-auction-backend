package com.neoga.boltauction.exception.custom;

import java.util.function.Supplier;

public class CCategoryNotFoundException extends RuntimeException {
    public CCategoryNotFoundException() {
        super("카테고리를 찾을 수 없습니다.");
    }

    public CCategoryNotFoundException(String message) {
        super(message);
    }
}
