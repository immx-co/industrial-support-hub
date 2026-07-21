package com.immx.industrialsupport.supportservice.exception_handling.user;

import com.immx.industrialsupport.supportservice.dto.ErrorCode;
import com.immx.industrialsupport.supportservice.dto.common.IncorrectResponseData;
import com.immx.industrialsupport.supportservice.exception_handling.ErrorCodeHttpStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Обработчик ошибок, связанных с пользователями подразделений.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class UserExceptionHandler {

    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    /**
     * Обработчик ошибки <code>UserAlreadyExistsException</code>.
     * @param exception возникшая ошибка <code>UserAlreadyExistsException</code>
     * @return ответ сервиса с ошибкой <code>UserAlreadyExistsException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(UserAlreadyExistsException exception) {
        ErrorCode errorCode = ErrorCode.ALREADY_EXISTS;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }
}
