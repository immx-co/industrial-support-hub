package com.immx.industrialsupport.supportservice.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import lombok.Getter;

/**
 * Модель ошибки некорректно заданной сущности.
 */
@Getter
public class IncorrectResponseData<T> extends BaseResponseData {

    /**
     * Детальная информация ошибки.
     */
    private final String info;

    /**
     * Тело ответа.
     */
    private final T data;

    /**
     * ctor класса IncorrectResponseData.
     *
     * @param errorCode Код ошибки ответа сервера.
     * @param info      Детальная информация ошибки.
     * @param data      Тело ответа.
     */
    @JsonCreator
    public IncorrectResponseData(@JsonProperty("errorCode") ErrorCode errorCode, @JsonProperty("info") String info,
                                 @JsonProperty("data") T data) {
        super(errorCode);
        this.info = info;
        this.data = data;
    }
}
