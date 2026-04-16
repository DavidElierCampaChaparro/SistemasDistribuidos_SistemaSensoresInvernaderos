package com.greenhouse.sensormanagementservice.exception;

import com.greenhouse.sensormanagementservice.dto.ErrorDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String businessErrorMessage = "BUSINESS_ERROR";
    private final String databaseErrorMessage = "DATABASE_ERROR";
    private final String validationErrorMessage = "VALIDATION_ERROR";
    private final String systemUnavailableErrorMessage = "SYSTEM_UNAVAILABLE";

    private final String duplicateEntryMessage = "Data conflict or duplicate entry.";
    private final String errorProcessingRequestMessage = "The system cannot process the request at this time.";

    // Business exception.
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException ex) {
        ErrorDTO error = new ErrorDTO(businessErrorMessage, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // 400
    }

    // Data integrity violation exception.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataError(DataIntegrityViolationException ex) {
        ErrorDTO error = new ErrorDTO(databaseErrorMessage, duplicateEntryMessage);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
    }

    // Invalid method argument exception.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorDTO errorDTO = new ErrorDTO(validationErrorMessage, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO); // 400
    }

    // Unavailable service exception.
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorDTO> handleServiceUnavailable(ServiceUnavailableException ex) {
        ErrorDTO error = new ErrorDTO(systemUnavailableErrorMessage, errorProcessingRequestMessage);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

}
