package com.menu.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryException.CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryException.CategoryNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(CategoryException.DuplicateDisplayOrderException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDisplayOrder(CategoryException.DuplicateDisplayOrderException ex) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(CategoryException.InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(CategoryException.InvalidOperationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(500).body(error);
    }
}