package com.immx.industrialsupport.supportservice.dto.common;

import com.immx.industrialsupport.supportservice.dto.ErrorCode;
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
