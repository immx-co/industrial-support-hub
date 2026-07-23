package com.immx.industrialsupport.supportservice.exception_handling.incident;

/**
 * Исключение, возникающее при невалидной операции, связанной с обращением.
 */
public class InvalidIncidentOperationException extends RuntimeException {
    public InvalidIncidentOperationException(String message) {
        super(message);
    }
}
