package org.example.scheduler.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-500", "서버 내부 오류가 발생했습니다."),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VAL-001","유효성 검증에 실패했습니다."),

    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "AUTH-401", "비밀번호가 올바르지 않습니다."),
    FORBIDDEN_NOT_OWNER(HttpStatus.FORBIDDEN, "AUTH-403", "접근 권한이 없습니다."),

    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "USER-001", "이미 가입된 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "존재하지 않는 사용자입니다."),

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCH-404", "존재하지 않는 일정입니다."),

    COMMENT_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "CMT-400","하나의 일정에 댓글은 최대 10개까지 등록할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
