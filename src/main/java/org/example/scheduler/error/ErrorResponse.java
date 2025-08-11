package org.example.scheduler.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 오류 정보를 응답할 때 사용하는 DTO
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public static ErrorResponse of(HttpStatusCode status, ErrorCode errorCode, String path, Map<String, String> errors) {
        return new ErrorResponse(status.value(), errorCode.getCode(), errorCode.getMessage(), path, LocalDateTime.now(), errors);
    }
    public static ErrorResponse of(HttpStatusCode status, String errorCode, String message, String path) {
        return new ErrorResponse(status.value(), errorCode, message, path, LocalDateTime.now(), null);
    }
}
