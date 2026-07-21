package com.immx.industrialsupport.supportservice.exception_handling.user;

/**
 * Исключение, возникающее, когда пользователь с <code>username</code> уже существует в подразделении.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
