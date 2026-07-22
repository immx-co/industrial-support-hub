package com.immx.industrialsupport.supportservice.exception_handling.department;

/**
 * Исключение, возникающее при уже существующем подразделении в конкретной организации.
 */
public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String message) {
        super(message);
    }
}
