package com.immx.industrialsupport.supportservice.exception_handling;

import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Маппер <code>ErrorCode</code> в <code>HttpStatus</code>.
 */
@Component
public class ErrorCodeHttpStatusMapper {

    /**
     * Маппает <code>ErrorCode</code> в <code>HttpStatus</code>.
     * @param errorCode код ошибки сервиса
     * @return <code>HTTP</code> статус код в зависимости от кода ошибки сервиса
     */
    public HttpStatus map(ErrorCode errorCode) {
        return switch (errorCode) {
            case SUCCESS -> HttpStatus.OK;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_REQUEST -> HttpStatus.INTERNAL_SERVER_ERROR;
            case DELETED -> HttpStatus.BAD_REQUEST;
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }
}
