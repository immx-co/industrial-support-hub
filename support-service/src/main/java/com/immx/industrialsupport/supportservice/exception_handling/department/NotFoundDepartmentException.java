package com.immx.industrialsupport.supportservice.exception_handling.department;

/**
 * Исключение, возникающее при не найденном подразделении.
 */
public class NotFoundDepartmentException extends RuntimeException {

    /**
     * ctor класса <code>NotFoundDepartmentException</code>.
     * @param message информационное сообщение ошибки
     */
    public NotFoundDepartmentException(String message) {
        super(message);
    }
}
