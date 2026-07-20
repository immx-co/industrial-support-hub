package com.immx.industrialsupport.supportservice.exception_handling;

import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Маппер ErrorCode в HttpStatus.
 */
@Component
public class ErrorCodeHttpStatusMapper {

    /**
     * Маппает ErrorCode в HttpStatus.
     * @param errorCode код ошибки сервиса
     * @return HTTP статус код в зависимости от кода ошибки сервиса
     */
    public HttpStatus map(ErrorCode errorCode) {
        return switch (errorCode) {
            case SUCCESS -> HttpStatus.OK;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_REQUEST, DELETED -> HttpStatus.BAD_REQUEST;
        };
    }
}
