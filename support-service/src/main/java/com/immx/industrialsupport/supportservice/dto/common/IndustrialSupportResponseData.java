package com.immx.industrialsupport.supportservice.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import lombok.Getter;

/**
 * Модель успешного ответа сервиса.
 *
 * @param <T> Generic параметр ответа сервиса
 */
@Getter
public class IndustrialSupportResponseData<T> extends BaseResponseData {

    /**
     * Информационное сообщение успешного ответа сервиса.
     */
    private final String message;

    /**
     * Данные ответа сервиса.
     */
    private final T data;

    public IndustrialSupportResponseData(@JsonProperty("message") String message, @JsonProperty("data") T data) {
        super(ErrorCode.SUCCESS);
        this.message = message;
        this.data = data;
    }
}
