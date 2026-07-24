package com.immx.industrialsupport.contracts.common;

import lombok.Getter;

/**
 * Базовая модель представления ответа сервиса.
 */
@Getter
public class BaseResponseData {

    /**
     * Статус код ошибки.
     */
    private final ErrorCode errorCode;

    public BaseResponseData(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
