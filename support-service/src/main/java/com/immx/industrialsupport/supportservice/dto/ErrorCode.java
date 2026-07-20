package com.immx.industrialsupport.supportservice.dto;

/**
 * Статус коды ошибок сервиса.
 */
public enum ErrorCode {

    /**
     * Успех.
     */
    SUCCESS,

    /**
     * Сущность не найдена.
     */
    NOT_FOUND,

    /**
     * Неудачный запрос.
     */
    INVALID_REQUEST,

    /**
     * Сущность удалена.
     */
    DELETED,
}
