package com.immx.industrialsupport.supportservice.exception_handling.incident;

/**
 * Исключение, возникающее при не нахождении обращения.
 */
public class NotFoundIncidentException extends RuntimeException {
    public NotFoundIncidentException(String message) {
        super(message);
    }
}
