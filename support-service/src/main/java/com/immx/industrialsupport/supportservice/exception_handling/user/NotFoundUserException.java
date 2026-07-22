package com.immx.industrialsupport.supportservice.exception_handling.user;

/**
 * Ошибка, возникающая при не найденном пользователе.
 */
public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
