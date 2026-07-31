package com.vn.test.demob1.LAB.util;

/**
 * Trùng khóa chính hoặc vướng ràng buộc khóa ngoại -> HTTP 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
