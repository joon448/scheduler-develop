package org.example.scheduler.dto.schedule;

import lombok.Getter;

/**
 * 일정 삭제 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleDeleteRequestDto {
    private String password;    // 비밀번호
}
