package org.example.scheduler.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 애플리케이션 전역 예외 Handler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,                                  // 어느 필드가
                        FieldError::getDefaultMessage,                         // 무슨 메시지로 실패했는지
                        (a, b) -> a,                             // 같은 필드는 첫 메시지 우선
                        LinkedHashMap::new                                     // 순서 유지
                ));

        ErrorResponse body = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, req.getRequestURI(), errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest req) {
        ErrorCode code = ex.getErrorCode();
        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()                 // ← 커스텀 메시지 우선
                : code.getMessage();
        ErrorResponse body = ErrorResponse.of(code.getStatus(), code.getCode(), message, req.getRequestURI());
        return ResponseEntity.status(code.getStatus()).body(body);
    }
}
