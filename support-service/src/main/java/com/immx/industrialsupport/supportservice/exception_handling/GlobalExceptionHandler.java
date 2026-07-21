package com.immx.industrialsupport.supportservice.exception_handling;

import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import com.immx.industrialsupport.supportservice.dto.common.IncorrectResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик ошибок.
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    /**
     * Обрабатывает неизвестное исключение.
     * @param exception исключение
     * @return ответ сервиса на неизвестное исключение.
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        IncorrectResponseData<Void> data = new IncorrectResponseData<Void>(
                errorCode,
                "Bad request " + exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }
}
