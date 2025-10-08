package com.menu.category;

public class CategoryException extends RuntimeException {
    private final String errorCode;

    public CategoryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static class CategoryNotFoundException extends CategoryException {
        public CategoryNotFoundException(String message) {
            super(message, "CATEGORY_NOT_FOUND");
        }
    }

    public static class DuplicateDisplayOrderException extends CategoryException {
        public DuplicateDisplayOrderException(String message) {
            super(message, "DUPLICATE_DISPLAY_ORDER");
        }
    }

    public static class InvalidOperationException extends CategoryException {
        public InvalidOperationException(String message) {
            super(message, "INVALID_OPERATION");
        }
    }
}