package com.immx.industrialsupport.supportservice.exception_handling.authorization;

import com.immx.industrialsupport.contracts.common.ErrorCode;
import com.immx.industrialsupport.contracts.common.IncorrectResponseData;
import com.immx.industrialsupport.supportservice.exception_handling.ErrorCodeHttpStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @Autowired
    private ErrorCodeHttpStatusMapper errorCodeHttpStatusMapper;

    @ExceptionHandler(
            { BadCredentialsException.class, DisabledException.class }
    )
    public ResponseEntity<IncorrectResponseData<Void>> handleAuthenticationException(AuthenticationException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        IncorrectResponseData<Void> response = new IncorrectResponseData<>(
                errorCode,
                "Invalid username or password",
                null);

        return ResponseEntity.status(errorCodeHttpStatusMapper.map(errorCode))
                .body(response);
    }
}
