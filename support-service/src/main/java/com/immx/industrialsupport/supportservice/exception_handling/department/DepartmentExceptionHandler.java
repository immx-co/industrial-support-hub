package com.immx.industrialsupport.supportservice.exception_handling.department;

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
 * Обработчик ошибок, связанных с работой с подразделениями организации.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class DepartmentExceptionHandler {

    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    /**
     * Обработчик ошибки <code>NotFoundDepartmentException</code>.
     *
     * @param exception возникшая ошибка <code>NotFoundDepartmentException</code>
     * @return ответ сервиса с ошибкой <code>NotFoundDepartmentException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(NotFoundDepartmentException exception) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }

    /**
     * Обработчик ошибки <code>DepartmentAlreadyExistsException</code>.
     *
     * @param exception возникшая ошибка <code>DepartmentAlreadyExistsException</code>
     * @return ответ сервиса с ошибкой <code>DepartmentAlreadyExistsException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(DepartmentAlreadyExistsException exception) {
        ErrorCode errorCode = ErrorCode.ALREADY_EXISTS;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }
}
