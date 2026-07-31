package com.vn.test.demob1.LAB.util;

/**
 * Dữ liệu nhập không hợp lệ -> HTTP 400.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
