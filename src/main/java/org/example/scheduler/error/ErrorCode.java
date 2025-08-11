package org.example.scheduler.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션에서 발생할 수 있는 오류 코드 정의
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-500", "서버 내부 오류가 발생했습니다."),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VAL-001","유효성 검증에 실패했습니다."),

    PASSWORD_SAME_AS_OLD(HttpStatus.BAD_REQUEST, "AUTH-400","새 비밀번호가 기존 비밀번호와 동일합니다."),
    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "AUTH-401", "비밀번호가 올바르지 않습니다."),
    FORBIDDEN_NOT_OWNER(HttpStatus.FORBIDDEN, "AUTH-403", "접근 권한이 없습니다."),

    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "USER-001", "이미 가입된 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "존재하지 않는 사용자입니다."),

    INVALID_PAGING_PARAM(HttpStatus.BAD_REQUEST, "SCH-400", "page/size 파라미터가 올바르지 않습니다. size는 최대 100까지 가능합니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCH-404", "존재하지 않는 일정입니다."),

    COMMENT_SCHEDULE_MISMATCH(HttpStatus.BAD_REQUEST, "CMT-400", "댓글이 요청한 일정에 속하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CMT-404", "존재하지 않는 댓글입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
