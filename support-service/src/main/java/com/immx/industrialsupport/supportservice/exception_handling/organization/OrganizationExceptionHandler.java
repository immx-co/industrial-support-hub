package com.immx.industrialsupport.supportservice.exception_handling.organization;

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
 * Обработчик ошибок, связанных с работой с организациями.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class OrganizationExceptionHandler {

    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    /**
     * Обработчик ошибки <code>NotFoundOrganizationException</code>.
     *
     * @param exception возникшая ошибка <code>NotFoundOrganizationException</code>
     * @return ответ сервиса с ошибкой <code>NotFoundOrganizationException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(NotFoundOrganizationException exception) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }

    /**
     * Обработчик ошибки <code>DeletedOrganizationException</code>.
     *
     * @param exception возникшая ошибка <code>DeletedOrganizationException</code>
     * @return ответ сервиса с ошибкой <code>DeletedOrganizationException</code>
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectResponseData<Void>> handleException(DeletedOrganizationException exception) {
        ErrorCode errorCode = ErrorCode.DELETED;
        IncorrectResponseData<Void> data = new IncorrectResponseData<>(
                errorCode,
                exception.getMessage(),
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(data);
    }
}
