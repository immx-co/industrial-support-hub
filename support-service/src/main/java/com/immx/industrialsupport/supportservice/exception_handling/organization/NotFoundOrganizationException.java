package com.immx.industrialsupport.supportservice.exception_handling.organization;

/**
 * Исключение, возникающее при не найденной организации.
 */
public class NotFoundOrganizationException extends RuntimeException {

    /**
     * ctor класса NotFoundOrganizationException.
     * @param message информационное сообщение ошибки
     */
    public NotFoundOrganizationException(String message) {
        super(message);
    }
}
