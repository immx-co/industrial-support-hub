package com.immx.industrialsupport.supportservice.exception_handling.incident;

import com.immx.industrialsupport.contracts.common.ErrorCode;
import com.immx.industrialsupport.contracts.common.IncorrectResponseData;
import com.immx.industrialsupport.supportservice.exception_handling.ErrorCodeHttpStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Обработчик ошибок, связанных с работой с обращениями.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class IncidentExceptionHandler {

    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    /**
     * Обработчик ошибки <code>NotFoundIncidentException</code>.
     *
     * @param exception возникшая ошибка <code>NotFoundIncidentException</code>
     * @return ответ сервиса с ошибкой <code>NotFoundIncidentException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(NotFoundIncidentException exception) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }

    /**
     * Обработчик ошибки <code>InvalidIncidentOperationException</code>.
     *
     * @param exception возникшая ошибка <code>InvalidIncidentOperationException</code>
     * @return ответ сервиса с ошибкой <code>InvalidIncidentOperationException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(InvalidIncidentOperationException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }
}
