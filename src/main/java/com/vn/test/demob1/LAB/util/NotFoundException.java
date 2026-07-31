package com.vn.test.demob1.LAB.util;

/**
 * Không tìm thấy bản ghi -> HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
