package com.immx.industrialsupport.supportservice.exception_handling.organization;

/**
 * Исключение, возникающее при удаленной сущности организации.
 */
public class DeletedOrganizationException extends RuntimeException {

    /**
     * ctor класса DeletedOrganizationException.
     * @param message информационное сообщение ошибки
     */
    public DeletedOrganizationException(String message) {
        super(message);
    }
}
