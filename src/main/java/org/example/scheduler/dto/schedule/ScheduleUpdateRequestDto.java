package org.example.scheduler.dto.schedule;

import lombok.Getter;

/**
 * 일정 수정 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleUpdateRequestDto {
    private String title;       // 제목
}
